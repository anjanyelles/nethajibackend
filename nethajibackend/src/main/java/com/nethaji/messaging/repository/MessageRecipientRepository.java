package com.nethaji.messaging.repository;

import com.nethaji.messaging.entity.MessageRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRecipientRepository extends JpaRepository<MessageRecipient, UUID> {
    List<MessageRecipient> findByCampaignId(UUID campaignId);

    long countByCampaignId(UUID campaignId);
}
