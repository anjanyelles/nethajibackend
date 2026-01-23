package com.nethaji.messaging.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethaji.messaging.entity.MessageDeliveryAttempt;
import com.nethaji.messaging.entity.MessageRecipient;
import com.nethaji.messaging.enums.DeliveryStatus;
import com.nethaji.messaging.enums.MessageChannel;
import com.nethaji.messaging.repository.MessageDeliveryAttemptRepository;
import com.nethaji.messaging.repository.MessageRecipientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nethaji-service/whatsapp/webhook")
@CrossOrigin
public class WhatsAppWebhookController {

    private final ObjectMapper objectMapper;
    private final MessageDeliveryAttemptRepository attemptRepository;
    private final MessageRecipientRepository recipientRepository;

    @Value("${whatsapp.webhook.verifyToken:}")
    private String verifyToken;

    @Value("${whatsapp.webhook.appSecret:}")
    private String appSecret;

    public WhatsAppWebhookController(
            ObjectMapper objectMapper,
            MessageDeliveryAttemptRepository attemptRepository,
            MessageRecipientRepository recipientRepository
    ) {
        this.objectMapper = objectMapper;
        this.attemptRepository = attemptRepository;
        this.recipientRepository = recipientRepository;
    }

    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge
    ) {
        if (mode != null && mode.equals("subscribe") && token != null && token.equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(403).body("forbidden");
    }

    @PostMapping
    public ResponseEntity<String> receive(
            @RequestBody String raw,
            @RequestHeader(name = "X-Hub-Signature-256", required = false) String signature
    ) {
        try {
            if (!isSignatureValid(raw, signature)) {
                return ResponseEntity.status(403).body("forbidden");
            }
            Map<String, Object> root = objectMapper.readValue(raw, Map.class);
            Object entryObj = root.get("entry");
            if (!(entryObj instanceof List<?> entryList)) {
                return ResponseEntity.ok("ok");
            }

            for (Object entry : entryList) {
                if (!(entry instanceof Map<?, ?> entryMap)) {
                    continue;
                }
                Object changesObj = entryMap.get("changes");
                if (!(changesObj instanceof List<?> changesList)) {
                    continue;
                }

                for (Object ch : changesList) {
                    if (!(ch instanceof Map<?, ?> changeMap)) {
                        continue;
                    }
                    Object valueObj = changeMap.get("value");
                    if (!(valueObj instanceof Map<?, ?> value)) {
                        continue;
                    }

                    Object statusesObj = value.get("statuses");
                    if (statusesObj instanceof List<?> statuses) {
                        for (Object st : statuses) {
                            if (st instanceof Map<?, ?> statusMap) {
                                handleStatus(statusMap);
                            }
                        }
                    }
                }
            }

            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.ok("ok");
        }
    }

    private boolean isSignatureValid(String rawBody, String signatureHeader) {
        // If no app secret configured, skip signature validation (use verify token on GET).
        if (appSecret == null || appSecret.isBlank()) {
            return true;
        }
        if (signatureHeader == null || signatureHeader.isBlank()) {
            return false;
        }
        // Header format: "sha256=<hex>"
        String prefix = "sha256=";
        if (!signatureHeader.startsWith(prefix)) {
            return false;
        }
        String theirHex = signatureHeader.substring(prefix.length()).trim();
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] digest = mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));
            String ourHex = toHex(digest);
            return MessageDigest.isEqual(ourHex.getBytes(StandardCharsets.UTF_8), theirHex.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return false;
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private void handleStatus(Map<?, ?> statusMap) {
        Object idObj = statusMap.get("id");
        if (idObj == null) {
            return;
        }
        String providerMessageId = String.valueOf(idObj);

        MessageDeliveryAttempt attempt = attemptRepository.findByProviderMessageId(providerMessageId).orElse(null);
        if (attempt == null) {
            return;
        }

        Object statusObj = statusMap.get("status");
        String status = statusObj == null ? null : String.valueOf(statusObj);
        DeliveryStatus ds = mapStatus(status);

        attempt.setStatus(ds);
        attempt.setUpdatedAt(new Date());
        attemptRepository.save(attempt);

        if (attempt.getChannel() == MessageChannel.WHATSAPP) {
            MessageRecipient r = recipientRepository.findById(attempt.getRecipientId()).orElse(null);
            if (r != null) {
                r.setWhatsappStatus(ds);
                recipientRepository.save(r);
            }
        }
    }

    private DeliveryStatus mapStatus(String waStatus) {
        if (waStatus == null) {
            return DeliveryStatus.SENT;
        }
        String s = waStatus.toLowerCase();
        if (s.equals("sent")) {
            return DeliveryStatus.SENT;
        }
        if (s.equals("delivered")) {
            return DeliveryStatus.DELIVERED;
        }
        if (s.equals("read")) {
            return DeliveryStatus.READ;
        }
        if (s.equals("failed")) {
            return DeliveryStatus.FAILED;
        }
        return DeliveryStatus.SENT;
    }
}
