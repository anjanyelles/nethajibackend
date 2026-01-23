package com.nethaji.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CampaignSendResponse {
    private UUID campaignId;
    private String status;
    private long recipientCount;
}
