package com.nethaji.entity;

import com.nethaji.Enums.Branch;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "student_education_info")
@Data
public class StudentEducationInfo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "student_semester",nullable = false)
    private Integer semester;

    @Column(name = "student_id",nullable = false)
    private UUID studentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester_status", nullable = false)
    private SemesterStatus semesterStatus = SemesterStatus.NOT_STARTED;

    @Column(name = "program_id")
    private UUID programId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Programs programs;


    @Column(name = "is_active")
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch")
    private Branch branch;

    @Column(name = "graduation_type")
    private String graduationType;


    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;




}

