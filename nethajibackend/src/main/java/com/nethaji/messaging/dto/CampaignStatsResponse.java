package com.nethaji.messaging.dto;

import com.nethaji.messaging.enums.CampaignStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CampaignStatsResponse {
    private UUID campaignId;
    private CampaignStatus status;
    private long recipients;
    private long outboxPending;
    private long outboxProcessing;
    private long outboxDone;
    private long outboxFailed;
}
