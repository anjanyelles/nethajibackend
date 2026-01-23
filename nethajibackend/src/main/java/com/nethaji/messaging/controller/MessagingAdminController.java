package com.nethaji.messaging.controller;

import com.nethaji.messaging.dto.CampaignSendResponse;
import com.nethaji.messaging.dto.CampaignStatsResponse;
import com.nethaji.messaging.dto.MessageCampaignRequest;
import com.nethaji.messaging.dto.MessageTemplateRequest;
import com.nethaji.messaging.entity.MessageCampaign;
import com.nethaji.messaging.entity.MessageDeliveryAttempt;
import com.nethaji.messaging.entity.MessageRecipient;
import com.nethaji.messaging.entity.MessageTemplate;
import com.nethaji.messaging.enums.DeliveryStatus;
import com.nethaji.messaging.enums.MessageChannel;
import com.nethaji.messaging.repository.MessageCampaignRepository;
import com.nethaji.messaging.repository.MessageDeliveryAttemptRepository;
import com.nethaji.messaging.repository.MessageRecipientRepository;
import com.nethaji.messaging.repository.MessageTemplateRepository;
import com.nethaji.messaging.service.MessagingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nethaji-service/messaging")
@CrossOrigin
public class MessagingAdminController {

    private final MessagingService messagingService;
    private final MessageTemplateRepository messageTemplateRepository;
    private final MessageCampaignRepository messageCampaignRepository;
    private final MessageRecipientRepository messageRecipientRepository;
    private final MessageDeliveryAttemptRepository messageDeliveryAttemptRepository;

    public MessagingAdminController(
            MessagingService messagingService,
            MessageTemplateRepository messageTemplateRepository,
            MessageCampaignRepository messageCampaignRepository,
            MessageRecipientRepository messageRecipientRepository,
            MessageDeliveryAttemptRepository messageDeliveryAttemptRepository
    ) {
        this.messagingService = messagingService;
        this.messageTemplateRepository = messageTemplateRepository;
        this.messageCampaignRepository = messageCampaignRepository;
        this.messageRecipientRepository = messageRecipientRepository;
        this.messageDeliveryAttemptRepository = messageDeliveryAttemptRepository;
    }

    @PostMapping("/templates")
    public ResponseEntity<MessageTemplate> createTemplate(@RequestParam UUID createdBy, @RequestBody MessageTemplateRequest req) {
        return ResponseEntity.ok(messagingService.createTemplate(createdBy, req));
    }

    @GetMapping("/templates")
    public ResponseEntity<List<MessageTemplate>> listTemplates() {
        return ResponseEntity.ok(messageTemplateRepository.findAll());
    }

    @PutMapping("/templates/{id}")
    public ResponseEntity<MessageTemplate> updateTemplate(@PathVariable UUID id, @RequestBody MessageTemplateRequest req) {
        MessageTemplate t = messagingService.updateTemplate(id, req);
        if (t == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(t);
    }

    @DeleteMapping("/templates/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable UUID id) {
        messageTemplateRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/campaigns")
    public ResponseEntity<MessageCampaign> createDraft(@RequestParam UUID createdBy, @RequestBody MessageCampaignRequest req) {
        return ResponseEntity.ok(messagingService.createDraft(createdBy, req));
    }

    @PostMapping("/campaigns/{id}/schedule")
    public ResponseEntity<MessageCampaign> schedule(@PathVariable UUID id, @RequestParam(required = false) Long scheduledAtMs) {
        Date at = scheduledAtMs == null ? new Date() : new Date(scheduledAtMs);
        MessageCampaign c = messagingService.schedule(id, at);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @PostMapping("/campaigns/{id}/enqueue")
    public ResponseEntity<CampaignSendResponse> enqueue(@PathVariable UUID id) {
        long count = messagingService.enqueueCampaign(id);
        return ResponseEntity.ok(new CampaignSendResponse(id, "ENQUEUED", count));
    }

    @PostMapping("/campaigns/{id}/cancel")
    public ResponseEntity<MessageCampaign> cancel(@PathVariable UUID id) {
        MessageCampaign c = messageCampaignRepository.findById(id).orElse(null);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        c.setStatus(com.nethaji.messaging.enums.CampaignStatus.CANCELLED);
        c.setUpdatedAt(new Date());
        return ResponseEntity.ok(messageCampaignRepository.save(c));
    }

    @PostMapping("/campaigns/send-now")
    public ResponseEntity<CampaignSendResponse> sendNow(@RequestParam UUID createdBy, @RequestBody MessageCampaignRequest req) {
        return ResponseEntity.ok(messagingService.sendNow(createdBy, req));
    }

    @GetMapping("/campaigns")
    public ResponseEntity<List<MessageCampaign>> listCampaigns() {
        return ResponseEntity.ok(messageCampaignRepository.findAll());
    }

    @GetMapping("/campaigns/{id}")
    public ResponseEntity<MessageCampaign> getCampaign(@PathVariable UUID id) {
        MessageCampaign c = messageCampaignRepository.findById(id).orElse(null);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @GetMapping("/campaigns/{id}/recipients")
    public ResponseEntity<List<MessageRecipient>> recipients(@PathVariable UUID id) {
        return ResponseEntity.ok(messageRecipientRepository.findByCampaignId(id));
    }

    @GetMapping("/campaigns/{id}/stats")
    public ResponseEntity<CampaignStatsResponse> stats(@PathVariable UUID id) {
        CampaignStatsResponse s = messagingService.stats(id);
        if (s == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(s);
    }

    @GetMapping("/campaigns/{id}/attempts")
    public ResponseEntity<List<MessageDeliveryAttempt>> attempts(@PathVariable UUID id) {
        return ResponseEntity.ok(messageDeliveryAttemptRepository.findByCampaignId(id));
    }

    @GetMapping("/campaigns/{id}/attempts/failed")
    public ResponseEntity<List<MessageDeliveryAttempt>> failedAttempts(
            @PathVariable UUID id,
            @RequestParam MessageChannel channel
    ) {
        return ResponseEntity.ok(
                messageDeliveryAttemptRepository.findByCampaignIdAndChannelAndStatus(id, channel, DeliveryStatus.FAILED)
        );
    }
}
