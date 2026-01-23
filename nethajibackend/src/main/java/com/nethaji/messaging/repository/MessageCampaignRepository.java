package com.nethaji.messaging.repository;

import com.nethaji.messaging.entity.MessageCampaign;
import com.nethaji.messaging.enums.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface MessageCampaignRepository extends JpaRepository<MessageCampaign, UUID> {
    List<MessageCampaign> findByStatus(CampaignStatus status);

    List<MessageCampaign> findByStatusAndScheduledAtLessThanEqual(CampaignStatus status, Date date);
}
