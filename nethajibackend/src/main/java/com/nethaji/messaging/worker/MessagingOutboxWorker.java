package com.nethaji.messaging.worker;

import com.nethaji.messaging.entity.MessageDeliveryAttempt;
import com.nethaji.messaging.entity.MessageOutbox;
import com.nethaji.messaging.entity.MessageRecipient;
import com.nethaji.messaging.enums.DeliveryStatus;
import com.nethaji.messaging.enums.MessageChannel;
import com.nethaji.messaging.enums.OutboxStatus;
import com.nethaji.messaging.enums.CampaignStatus;
import com.nethaji.messaging.provider.EmailProvider;
import com.nethaji.messaging.provider.EmailSendResult;
import com.nethaji.messaging.provider.SmsProvider;
import com.nethaji.messaging.provider.SmsSendResult;
import com.nethaji.messaging.provider.WhatsAppProvider;
import com.nethaji.messaging.provider.WhatsAppSendResult;
import com.nethaji.messaging.repository.MessageCampaignRepository;
import com.nethaji.messaging.repository.MessageDeliveryAttemptRepository;
import com.nethaji.messaging.repository.MessageOutboxRepository;
import com.nethaji.messaging.repository.MessageRecipientRepository;
import com.nethaji.messaging.service.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessagingOutboxWorker {

    private static final Logger log = LoggerFactory.getLogger(MessagingOutboxWorker.class);

    private final MessageOutboxRepository messageOutboxRepository;
    private final MessageRecipientRepository messageRecipientRepository;
    private final MessageDeliveryAttemptRepository messageDeliveryAttemptRepository;
    private final EmailProvider emailProvider;
    private final SmsProvider smsProvider;
    private final WhatsAppProvider whatsAppProvider;
    private final MessagingService messagingService;
    private final MessageCampaignRepository messageCampaignRepository;

    public MessagingOutboxWorker(
            MessageOutboxRepository messageOutboxRepository,
            MessageRecipientRepository messageRecipientRepository,
            MessageDeliveryAttemptRepository messageDeliveryAttemptRepository,
            EmailProvider emailProvider,
            SmsProvider smsProvider,
            WhatsAppProvider whatsAppProvider,
            MessagingService messagingService,
            MessageCampaignRepository messageCampaignRepository
    ) {
        this.messageOutboxRepository = messageOutboxRepository;
        this.messageRecipientRepository = messageRecipientRepository;
        this.messageDeliveryAttemptRepository = messageDeliveryAttemptRepository;
        this.emailProvider = emailProvider;
        this.smsProvider = smsProvider;
        this.whatsAppProvider = whatsAppProvider;
        this.messagingService = messagingService;
        this.messageCampaignRepository = messageCampaignRepository;
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void scheduleDueCampaigns() {
        Date now = new Date();
        var due = messageCampaignRepository.findByStatusAndScheduledAtLessThanEqual(CampaignStatus.SCHEDULED, now);
        for (var c : due) {
            try {
                messagingService.enqueueCampaign(c.getId());
            } catch (Exception ex) {
                log.error("Failed to enqueue scheduled campaign {}", c.getId(), ex);
            }
        }
    }

    @Scheduled(fixedDelay = 1500)
    @Transactional
    public void processOutbox() {
        List<MessageOutbox> batch = messageOutboxRepository.findReady(OutboxStatus.PENDING, new Date());
        int limit = Math.min(batch.size(), 30);
        for (int i = 0; i < limit; i++) {
            handle(batch.get(i));
        }

        // Mark sending campaigns as completed when there is no work left.
        // (Simple heuristic for MVP)
        var sending = messageCampaignRepository.findByStatus(CampaignStatus.SENDING);
        for (var c : sending) {
            long pending = messageOutboxRepository.countByCampaignIdAndStatus(c.getId(), OutboxStatus.PENDING);
            long processing = messageOutboxRepository.countByCampaignIdAndStatus(c.getId(), OutboxStatus.PROCESSING);
            if (pending == 0 && processing == 0) {
                c.setStatus(CampaignStatus.COMPLETED);
                c.setUpdatedAt(new Date());
                messageCampaignRepository.save(c);
            }
        }
    }

    private void handle(MessageOutbox o) {
        try {
            o.setStatus(OutboxStatus.PROCESSING);
            o.setLockedAt(new Date());
            o.setLockedBy("worker");
            messageOutboxRepository.save(o);

            MessageRecipient r = messageRecipientRepository.findById(o.getRecipientId()).orElse(null);
            if (r == null) {
                o.setStatus(OutboxStatus.FAILED);
                o.setLastError("Recipient not found");
                messageOutboxRepository.save(o);
                return;
            }

            int attemptNo = nextAttemptNo(r.getId(), o.getChannel());
            MessageDeliveryAttempt attempt = new MessageDeliveryAttempt();
            attempt.setRecipientId(r.getId());
            attempt.setChannel(o.getChannel());
            attempt.setAttemptNo(attemptNo);
            attempt.setStatus(DeliveryStatus.PROCESSING);
            attempt.setRequestedAt(new Date());
            attempt.setUpdatedAt(new Date());
            messageDeliveryAttemptRepository.save(attempt);

            if (o.getChannel() == MessageChannel.EMAIL) {
                EmailSendResult res = emailProvider.send(r.getEmail(), r.getRenderedSubject(), r.getRenderedBody());
                if (res.isAccepted()) {
                    attempt.setProvider("SES");
                    attempt.setProviderMessageId(res.getProviderMessageId());
                    attempt.setStatus(DeliveryStatus.SENT);
                    attempt.setUpdatedAt(new Date());
                    messageDeliveryAttemptRepository.save(attempt);

                    r.setEmailStatus(DeliveryStatus.SENT);
                    messageRecipientRepository.save(r);

                    o.setStatus(OutboxStatus.DONE);
                    messageOutboxRepository.save(o);
                } else {
                    markFailed(o, r, attempt, res.getErrorMessage());
                }
                return;
            }

            if (o.getChannel() == MessageChannel.SMS) {
                SmsSendResult res = smsProvider.send(r.getMobile(), r.getRenderedBody());
                if (res.isAccepted()) {
                    attempt.setProvider("SMS");
                    attempt.setProviderMessageId(res.getProviderMessageId());
                    attempt.setStatus(DeliveryStatus.SENT);
                    attempt.setUpdatedAt(new Date());
                    messageDeliveryAttemptRepository.save(attempt);

                    r.setSmsStatus(DeliveryStatus.SENT);
                    messageRecipientRepository.save(r);

                    o.setStatus(OutboxStatus.DONE);
                    messageOutboxRepository.save(o);
                } else {
                    markFailed(o, r, attempt, res.getErrorMessage());
                }
                return;
            }

            if (o.getChannel() == MessageChannel.WHATSAPP) {
                // WhatsApp Cloud API expects E.164. For India: +91XXXXXXXXXX; Meta API uses digits without plus.
                String to = normalizeToE164Digits(r.getMobile());
                String templateName = r.getRenderedSubject();
                String bodyParam = r.getRenderedBody();

                WhatsAppSendResult res = whatsAppProvider.sendTemplate(to, templateName, "en_US", bodyParam);
                if (res.isAccepted()) {
                    attempt.setProvider("META");
                    attempt.setProviderMessageId(res.getProviderMessageId());
                    attempt.setStatus(DeliveryStatus.SENT);
                    attempt.setUpdatedAt(new Date());
                    messageDeliveryAttemptRepository.save(attempt);

                    r.setWhatsappStatus(DeliveryStatus.SENT);
                    messageRecipientRepository.save(r);

                    o.setStatus(OutboxStatus.DONE);
                    messageOutboxRepository.save(o);
                } else {
                    markFailed(o, r, attempt, res.getErrorMessage());
                }
                return;
            }

            o.setStatus(OutboxStatus.DONE);
            messageOutboxRepository.save(o);

        } catch (Exception ex) {
            log.error("Outbox processing failed", ex);
            o.setStatus(OutboxStatus.FAILED);
            o.setLastError(ex.getMessage());
            messageOutboxRepository.save(o);
        }
    }

    private void markFailed(MessageOutbox o, MessageRecipient r, MessageDeliveryAttempt attempt, String err) {
        attempt.setStatus(DeliveryStatus.FAILED);
        attempt.setErrorMessage(err);
        attempt.setUpdatedAt(new Date());
        messageDeliveryAttemptRepository.save(attempt);

        if (o.getChannel() == MessageChannel.EMAIL) {
            r.setEmailStatus(DeliveryStatus.FAILED);
        }
        if (o.getChannel() == MessageChannel.SMS) {
            r.setSmsStatus(DeliveryStatus.FAILED);
        }
        if (o.getChannel() == MessageChannel.WHATSAPP) {
            r.setWhatsappStatus(DeliveryStatus.FAILED);
        }
        messageRecipientRepository.save(r);

        int retry = o.getRetryCount() == null ? 0 : o.getRetryCount();
        if (retry >= 3) {
            o.setStatus(OutboxStatus.FAILED);
            o.setLastError(err);
            messageOutboxRepository.save(o);
            return;
        }

        o.setStatus(OutboxStatus.PENDING);
        o.setRetryCount(retry + 1);
        o.setLastError(err);
        o.setNextRetryAt(new Date(System.currentTimeMillis() + (long) (Math.pow(2, retry) * 60_000L)));
        messageOutboxRepository.save(o);
    }

    private int nextAttemptNo(UUID recipientId, MessageChannel channel) {
        List<MessageDeliveryAttempt> prev = messageDeliveryAttemptRepository.findByRecipientIdAndChannelOrderByAttemptNoDesc(recipientId, channel);
        if (prev == null || prev.isEmpty()) {
            return 1;
        }
        Integer n = prev.get(0).getAttemptNo();
        return n == null ? 1 : n + 1;
    }

    private String normalizeToE164Digits(String mobile) {
        if (mobile == null) {
            return "";
        }
        String digits = mobile.replaceAll("[^0-9]", "");
        if (digits.length() == 10) {
            return "91" + digits;
        }
        return digits;
    }
}
