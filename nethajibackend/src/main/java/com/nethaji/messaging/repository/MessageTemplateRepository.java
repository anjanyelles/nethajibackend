package com.nethaji.messaging.repository;

import com.nethaji.messaging.entity.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, UUID> {
    Optional<MessageTemplate> findByCode(String code);
}
