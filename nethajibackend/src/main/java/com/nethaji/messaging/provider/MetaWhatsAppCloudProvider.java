package com.nethaji.messaging.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MetaWhatsAppCloudProvider implements WhatsAppProvider {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String graphBaseUrl;
    private final String phoneNumberId;
    private final String accessToken;

    public MetaWhatsAppCloudProvider(
            ObjectMapper objectMapper,
            @Value("${whatsapp.graph.baseUrl:https://graph.facebook.com/v19.0}") String graphBaseUrl,
            @Value("${whatsapp.phoneNumberId:}") String phoneNumberId,
            @Value("${whatsapp.accessToken:}") String accessToken
    ) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
        this.graphBaseUrl = graphBaseUrl;
        this.phoneNumberId = phoneNumberId;
        this.accessToken = accessToken;
    }

    @Override
    public WhatsAppSendResult sendTemplate(String toMobileE164, String templateName, String languageCode, String bodyText) {
        if (phoneNumberId == null || phoneNumberId.isBlank()) {
            return new WhatsAppSendResult(false, null, "CONFIG_ERROR", "Missing whatsapp.phoneNumberId");
        }
        if (accessToken == null || accessToken.isBlank()) {
            return new WhatsAppSendResult(false, null, "CONFIG_ERROR", "Missing whatsapp.accessToken");
        }
        if (toMobileE164 == null || toMobileE164.isBlank()) {
            return new WhatsAppSendResult(false, null, "INVALID_TO", "Recipient mobile is empty");
        }
        if (templateName == null || templateName.isBlank()) {
            return new WhatsAppSendResult(false, null, "INVALID_TEMPLATE", "Template name is empty");
        }

        try {
            String url = graphBaseUrl + "/" + phoneNumberId + "/messages";

            Map<String, Object> payload = new HashMap<>();
            payload.put("messaging_product", "whatsapp");
            payload.put("to", toMobileE164);
            payload.put("type", "template");

            Map<String, Object> template = new HashMap<>();
            template.put("name", templateName);

            Map<String, Object> language = new HashMap<>();
            language.put("code", (languageCode == null || languageCode.isBlank()) ? "en_US" : languageCode);
            template.put("language", language);

            if (bodyText != null && !bodyText.isBlank()) {
                Map<String, Object> param = new HashMap<>();
                param.put("type", "text");
                param.put("text", bodyText);

                Map<String, Object> body = new HashMap<>();
                body.put("type", "body");
                body.put("parameters", List.of(param));

                template.put("components", List.of(body));
            }

            payload.put("template", template);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> resp = restTemplate.postForEntity(url, entity, String.class);

            if (!resp.getStatusCode().is2xxSuccessful()) {
                return new WhatsAppSendResult(false, null, "HTTP_" + resp.getStatusCode().value(), resp.getBody());
            }

            String providerMessageId = extractMessageId(resp.getBody());
            return new WhatsAppSendResult(true, providerMessageId, null, null);

        } catch (Exception ex) {
            return new WhatsAppSendResult(false, null, "WHATSAPP_ERROR", ex.getMessage());
        }
    }

    private String extractMessageId(String body) {
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            Map<?, ?> parsed = objectMapper.readValue(body, Map.class);
            Object messages = parsed.get("messages");
            if (messages instanceof List<?> list && !list.isEmpty()) {
                Object first = list.get(0);
                if (first instanceof Map<?, ?> m) {
                    Object id = m.get("id");
                    return id == null ? null : String.valueOf(id);
                }
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }
}
