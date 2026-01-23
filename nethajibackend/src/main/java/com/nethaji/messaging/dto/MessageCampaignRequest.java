package com.nethaji.messaging.dto;

import com.nethaji.messaging.enums.MessageChannel;
import com.nethaji.messaging.enums.TargetingType;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class MessageCampaignRequest {
    private String title;
    private UUID templateId;
    private String subject;
    private String body;
    private TargetingType targetingType;
    private Map<String, Object> targeting;
    private List<MessageChannel> channels;
    private Date scheduledAt;
}
