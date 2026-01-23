package com.nethaji.messaging.repository;

import com.nethaji.messaging.entity.MessageDeliveryAttempt;
import com.nethaji.messaging.enums.DeliveryStatus;
import com.nethaji.messaging.enums.MessageChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageDeliveryAttemptRepository extends JpaRepository<MessageDeliveryAttempt, UUID> {
    List<MessageDeliveryAttempt> findByRecipientIdAndChannelOrderByAttemptNoDesc(UUID recipientId, MessageChannel channel);

    Optional<MessageDeliveryAttempt> findByProviderMessageId(String providerMessageId);

    @Query("SELECT a FROM MessageDeliveryAttempt a WHERE a.recipientId IN (SELECT r.id FROM MessageRecipient r WHERE r.campaignId = :campaignId) ORDER BY a.requestedAt DESC")
    List<MessageDeliveryAttempt> findByCampaignId(@Param("campaignId") UUID campaignId);

    @Query("SELECT a FROM MessageDeliveryAttempt a WHERE a.recipientId IN (SELECT r.id FROM MessageRecipient r WHERE r.campaignId = :campaignId) AND a.channel = :channel AND a.status = :status ORDER BY a.requestedAt DESC")
    List<MessageDeliveryAttempt> findByCampaignIdAndChannelAndStatus(
            @Param("campaignId") UUID campaignId,
            @Param("channel") MessageChannel channel,
            @Param("status") DeliveryStatus status
    );
}
