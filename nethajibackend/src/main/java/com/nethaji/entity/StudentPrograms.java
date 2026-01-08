package com.nethaji.entity;

import com.nethaji.Enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "student_programs")
@Data
public class StudentPrograms {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;


    @Column(name = "program_id",nullable = false)
    private UUID programId;


    @Column(name = "student_id",nullable = false)
    private UUID studentId;


    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status")
    private EnrollmentStatus enrollmentStatus = EnrollmentStatus.NOT_STARTED;


    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Programs program;



    @Column(name = "is_active")
    private boolean isActive = true;


    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


}

