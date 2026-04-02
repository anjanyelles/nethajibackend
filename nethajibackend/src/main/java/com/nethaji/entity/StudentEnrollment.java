package com.nethaji.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "student_enrollment")
public class StudentEnrollment {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "hall_ticket_no")
    private String hallTicketNo;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "inter_group_college")
    private String interGroupCollege;

    @Column(name = "aadhar_no")
    private String aadharNo;

    @NotBlank(message = "Aadhar mobile is required")
    @Column(name = "aadhar_mobile", nullable = false)
    private String aadharMobile;

    @Column(name = "whatsapp_no")
    private String whatsappNo;

    @Column(name = "address")
    private String address;

    @Column(name = "village")
    private String village;

    @Column(name = "courses", columnDefinition = "TEXT")
    private String courses; // Stored as JSON or comma-separated string

    @Column(name = "referred_by")
    private String referredBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
