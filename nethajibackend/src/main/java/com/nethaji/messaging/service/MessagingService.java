package com.nethaji.messaging.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethaji.entity.StudentPrograms;
import com.nethaji.entity.User;
import com.nethaji.messaging.dto.CampaignSendResponse;
import com.nethaji.messaging.dto.CampaignStatsResponse;
import com.nethaji.messaging.dto.MessageCampaignRequest;
import com.nethaji.messaging.dto.MessageTemplateRequest;
import com.nethaji.messaging.entity.InboxMessage;
import com.nethaji.messaging.entity.MessageCampaign;
import com.nethaji.messaging.entity.MessageOutbox;
import com.nethaji.messaging.entity.MessageRecipient;
import com.nethaji.messaging.entity.MessageTemplate;
import com.nethaji.messaging.enums.CampaignStatus;
import com.nethaji.messaging.enums.MessageChannel;
import com.nethaji.messaging.enums.OutboxStatus;
import com.nethaji.messaging.enums.TargetingType;
import com.nethaji.messaging.repository.InboxMessageRepository;
import com.nethaji.messaging.repository.MessageCampaignRepository;
import com.nethaji.messaging.repository.MessageOutboxRepository;
import com.nethaji.messaging.repository.MessageRecipientRepository;
import com.nethaji.messaging.repository.MessageTemplateRepository;
import com.nethaji.repositories.StudentProgramsRepository;
import com.nethaji.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MessagingService {

    private final ObjectMapper objectMapper;
    private final TemplateRenderer templateRenderer;

    private final MessageTemplateRepository messageTemplateRepository;
    private final MessageCampaignRepository messageCampaignRepository;
    private final MessageRecipientRepository messageRecipientRepository;
    private final MessageOutboxRepository messageOutboxRepository;
    private final InboxMessageRepository inboxMessageRepository;

    private final UserRepository userRepository;
    private final StudentProgramsRepository studentProgramsRepository;

    public MessagingService(
            ObjectMapper objectMapper,
            TemplateRenderer templateRenderer,
            MessageTemplateRepository messageTemplateRepository,
            MessageCampaignRepository messageCampaignRepository,
            MessageRecipientRepository messageRecipientRepository,
            MessageOutboxRepository messageOutboxRepository,
            InboxMessageRepository inboxMessageRepository,
            UserRepository userRepository,
            StudentProgramsRepository studentProgramsRepository
    ) {
        this.objectMapper = objectMapper;
        this.templateRenderer = templateRenderer;
        this.messageTemplateRepository = messageTemplateRepository;
        this.messageCampaignRepository = messageCampaignRepository;
        this.messageRecipientRepository = messageRecipientRepository;
        this.messageOutboxRepository = messageOutboxRepository;
        this.inboxMessageRepository = inboxMessageRepository;
        this.userRepository = userRepository;
        this.studentProgramsRepository = studentProgramsRepository;
    }

    @Transactional
    public MessageTemplate createTemplate(UUID createdBy, MessageTemplateRequest req) {
        MessageTemplate t = new MessageTemplate();
        t.setCode(req.getCode());
        t.setName(req.getName());
        t.setDescription(req.getDescription());
        t.setDefaultSubject(req.getDefaultSubject());
        t.setDefaultBody(req.getDefaultBody());
        t.setCreatedBy(createdBy);
        t.setCreatedAt(new Date());
        t.setUpdatedAt(new Date());
        return messageTemplateRepository.save(t);
    }

    @Transactional
    public MessageTemplate updateTemplate(UUID id, MessageTemplateRequest req) {
        MessageTemplate t = messageTemplateRepository.findById(id).orElse(null);
        if (t == null) {
            return null;
        }
        t.setCode(req.getCode());
        t.setName(req.getName());
        t.setDescription(req.getDescription());
        t.setDefaultSubject(req.getDefaultSubject());
        t.setDefaultBody(req.getDefaultBody());
        t.setUpdatedAt(new Date());
        return messageTemplateRepository.save(t);
    }

    @Transactional
    public MessageCampaign createDraft(UUID createdBy, MessageCampaignRequest req) {
        MessageCampaign c = new MessageCampaign();
        c.setTitle(req.getTitle());
        c.setTemplateId(req.getTemplateId());
        c.setSubject(req.getSubject());
        c.setBody(resolveBodyFromTemplateIfNeeded(req));
        c.setTargetingType(req.getTargetingType());
        c.setTargetingJson(toJson(req.getTargeting()));
        c.setChannels(toJson(req.getChannels()));
        c.setStatus(CampaignStatus.DRAFT);
        c.setScheduledAt(req.getScheduledAt());
        c.setCreatedBy(createdBy);
        c.setCreatedAt(new Date());
        c.setUpdatedAt(new Date());
        return messageCampaignRepository.save(c);
    }

    @Transactional
    public MessageCampaign schedule(UUID campaignId, Date scheduledAt) {
        MessageCampaign c = messageCampaignRepository.findById(campaignId).orElse(null);
        if (c == null) {
            return null;
        }
        c.setScheduledAt(scheduledAt);
        c.setStatus(CampaignStatus.SCHEDULED);
        c.setUpdatedAt(new Date());
        return messageCampaignRepository.save(c);
    }

    @Transactional
    public CampaignSendResponse sendNow(UUID createdBy, MessageCampaignRequest req) {
        MessageCampaign c = new MessageCampaign();
        c.setTitle(req.getTitle());
        c.setTemplateId(req.getTemplateId());
        c.setSubject(req.getSubject());
        c.setBody(resolveBodyFromTemplateIfNeeded(req));
        c.setTargetingType(req.getTargetingType());
        c.setTargetingJson(toJson(req.getTargeting()));
        c.setChannels(toJson(req.getChannels()));
        c.setStatus(CampaignStatus.SENDING);
        c.setScheduledAt(null);
        c.setCreatedBy(createdBy);
        c.setCreatedAt(new Date());
        c.setUpdatedAt(new Date());
        c = messageCampaignRepository.save(c);

        long recipients = enqueueRecipientsAndOutbox(c, req.getChannels(), req.getTargetingType(), req.getTargeting());

        return new CampaignSendResponse(c.getId(), c.getStatus().name(), recipients);
    }

    @Transactional
    public long enqueueCampaign(UUID campaignId) {
        MessageCampaign c = messageCampaignRepository.findById(campaignId).orElse(null);
        if (c == null) {
            return 0;
        }

        Map<String, Object> targeting = fromJsonMap(c.getTargetingJson());
        List<MessageChannel> channels = fromJsonChannels(c.getChannels());

        c.setStatus(CampaignStatus.SENDING);
        c.setUpdatedAt(new Date());
        messageCampaignRepository.save(c);

        return enqueueRecipientsAndOutbox(c, channels, c.getTargetingType(), targeting);
    }

    public CampaignStatsResponse stats(UUID campaignId) {
        MessageCampaign c = messageCampaignRepository.findById(campaignId).orElse(null);
        if (c == null) {
            return null;
        }
        long recipients = messageRecipientRepository.countByCampaignId(campaignId);
        long pending = messageOutboxRepository.countByCampaignIdAndStatus(campaignId, OutboxStatus.PENDING);
        long processing = messageOutboxRepository.countByCampaignIdAndStatus(campaignId, OutboxStatus.PROCESSING);
        long done = messageOutboxRepository.countByCampaignIdAndStatus(campaignId, OutboxStatus.DONE);
        long failed = messageOutboxRepository.countByCampaignIdAndStatus(campaignId, OutboxStatus.FAILED);
        return new CampaignStatsResponse(campaignId, c.getStatus(), recipients, pending, processing, done, failed);
    }

    private long enqueueRecipientsAndOutbox(
            MessageCampaign campaign,
            List<MessageChannel> channels,
            TargetingType targetingType,
            Map<String, Object> targeting
    ) {
        boolean hasOutboxChannels = false;
        if (channels != null) {
            for (MessageChannel ch : channels) {
                if (ch != MessageChannel.IN_APP) {
                    hasOutboxChannels = true;
                    break;
                }
            }
        }

        List<User> users = resolveRecipients(targetingType, targeting);
        Date now = new Date();

        for (User u : users) {
            MessageRecipient r = new MessageRecipient();
            r.setCampaignId(campaign.getId());
            r.setUserId(u.getId());
            r.setEmail(u.getEmail());
            r.setMobile(u.getMobileNumber());

            Map<String, String> vars = buildVarsForUser(u);
            String renderedSubject = templateRenderer.render(resolveSubject(campaign), vars);
            String renderedBody = templateRenderer.render(campaign.getBody(), vars);
            r.setRenderedSubject(renderedSubject);
            r.setRenderedBody(renderedBody);
            r.setCreatedAt(now);
            r = messageRecipientRepository.save(r);

            if (channels != null) {
                for (MessageChannel ch : channels) {
                    if (ch == MessageChannel.IN_APP) {
                        InboxMessage inbox = new InboxMessage();
                        inbox.setUserId(u.getId());
                        inbox.setCampaignId(campaign.getId());
                        inbox.setRecipientId(r.getId());
                        inbox.setTitle(renderedSubject != null ? renderedSubject : campaign.getTitle());
                        inbox.setBody(renderedBody);
                        inbox.setCreatedAt(now);
                        inboxMessageRepository.save(inbox);
                    } else {
                        MessageOutbox o = new MessageOutbox();
                        o.setCampaignId(campaign.getId());
                        o.setRecipientId(r.getId());
                        o.setChannel(ch);
                        o.setNextRetryAt(now);
                        o.setCreatedAt(now);
                        messageOutboxRepository.save(o);
                    }
                }
            }
        }

        if (!hasOutboxChannels) {
            campaign.setStatus(CampaignStatus.COMPLETED);
            campaign.setUpdatedAt(new Date());
            messageCampaignRepository.save(campaign);
        }

        return users.size();
    }

    private List<User> resolveRecipients(TargetingType type, Map<String, Object> targeting) {
        if (type == null) {
            return new ArrayList<>();
        }

        if (type == TargetingType.ALL) {
            return userRepository.findUsersByType(User.UserType.STUDENT);
        }

        if (type == TargetingType.YEAR) {
            Integer year = tryParseInt(targeting == null ? null : targeting.get("joiningYear"));
            if (year == null) {
                return new ArrayList<>();
            }
            return userRepository.findUsersByTypeAndJoiningYear(User.UserType.STUDENT, year);
        }

        if (type == TargetingType.SELECTED) {
            List<UUID> ids = tryParseUuidList(targeting == null ? null : targeting.get("userIds"));
            if (ids.isEmpty()) {
                return new ArrayList<>();
            }
            return userRepository.findUsersByTypeAndIdIn(User.UserType.STUDENT, ids);
        }

        if (type == TargetingType.PROGRAM) {
            List<UUID> programIds = tryParseUuidList(targeting == null ? null : targeting.get("programIds"));
            if (programIds.isEmpty()) {
                return new ArrayList<>();
            }
            List<User> out = new ArrayList<>();
            for (UUID pid : programIds) {
                List<StudentPrograms> sps = studentProgramsRepository.findByProgramIdAndIsActiveTrue(pid);
                for (StudentPrograms sp : sps) {
                    User u = sp.getStudent();
                    if (u != null && u.getUserType() == User.UserType.STUDENT) {
                        out.add(u);
                    }
                }
            }
            return out;
        }

        return new ArrayList<>();
    }

    private Map<String, String> buildVarsForUser(User u) {
        Map<String, String> vars = new HashMap<>();
        vars.put("firstName", u.getFirstName());
        vars.put("lastName", u.getLastName());
        vars.put("email", u.getEmail());
        vars.put("enrollmentNumber", u.getEnrollmentNumber());
        vars.put("joiningYear", u.getJoiningYear() == null ? null : String.valueOf(u.getJoiningYear()));
        return vars;
    }

    private String resolveSubject(MessageCampaign campaign) {
        if (campaign.getSubject() != null && !campaign.getSubject().isBlank()) {
            return campaign.getSubject();
        }
        return campaign.getTitle();
    }

    private String resolveBodyFromTemplateIfNeeded(MessageCampaignRequest req) {
        if (req.getBody() != null && !req.getBody().isBlank()) {
            return req.getBody();
        }
        if (req.getTemplateId() == null) {
            return "";
        }
        MessageTemplate t = messageTemplateRepository.findById(req.getTemplateId()).orElse(null);
        if (t == null) {
            return "";
        }
        if (req.getSubject() == null || req.getSubject().isBlank()) {
            req.setSubject(t.getDefaultSubject());
        }
        return t.getDefaultBody();
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj == null ? new Object() : obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> fromJsonMap(String json) {
        if (json == null || json.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private List<MessageChannel> fromJsonChannels(String json) {
        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }
        try {
            List<String> raw = objectMapper.readValue(json, List.class);
            List<MessageChannel> out = new ArrayList<>();
            for (String s : raw) {
                try {
                    out.add(MessageChannel.valueOf(s));
                } catch (Exception ignored) {
                }
            }
            return out;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private Integer tryParseInt(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof Integer i) {
            return i;
        }
        if (v instanceof Number n) {
            return n.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(v));
        } catch (Exception e) {
            return null;
        }
    }

    private List<UUID> tryParseUuidList(Object v) {
        List<UUID> out = new ArrayList<>();
        if (v == null) {
            return out;
        }
        if (v instanceof List<?> list) {
            for (Object item : list) {
                UUID id = tryParseUuid(item);
                if (id != null) {
                    out.add(id);
                }
            }
            return out;
        }
        UUID single = tryParseUuid(v);
        if (single != null) {
            out.add(single);
        }
        return out;
    }

    private UUID tryParseUuid(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof UUID u) {
            return u;
        }
        try {
            return UUID.fromString(String.valueOf(v));
        } catch (Exception e) {
            return null;
        }
    }
}
