package com.nethaji.messaging.entity;

import com.nethaji.messaging.enums.CampaignStatus;
import com.nethaji.messaging.enums.TargetingType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "message_campaign")
@Data
public class MessageCampaign {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "template_id")
    private UUID templateId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body", columnDefinition = "TEXT", nullable = false)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "targeting_type", nullable = false)
    private TargetingType targetingType;

    @Column(name = "targeting_json", columnDefinition = "TEXT", nullable = false)
    private String targetingJson;

    @Column(name = "channels", columnDefinition = "TEXT", nullable = false)
    private String channels;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CampaignStatus status;

    @Column(name = "scheduled_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
