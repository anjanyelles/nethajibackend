package com.nethaji.messaging.repository;

import com.nethaji.messaging.entity.MessageOutbox;
import com.nethaji.messaging.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface MessageOutboxRepository extends JpaRepository<MessageOutbox, UUID> {

    @Query("SELECT o FROM MessageOutbox o WHERE o.status = :status AND o.nextRetryAt <= :now ORDER BY o.createdAt ASC")
    List<MessageOutbox> findReady(@Param("status") OutboxStatus status, @Param("now") Date now);

    long countByCampaignIdAndStatus(UUID campaignId, OutboxStatus status);
}
