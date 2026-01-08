package com.nethaji.entity;

import com.nethaji.Enums.ExamType;
import com.nethaji.Enums.TimeTableType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;


@Entity
@Data
@Table(name = "exames_time_table")
public class ExamesTimeTable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "exam_time", nullable = true)
    private String examTime;

    @Column(name = "exam_subject", nullable = true)
    private String examSubject;

    @Column(name = "exam_center", nullable = true)
    private String examCenter;

    @Column(name = "department_id", nullable = true)
    private UUID departmentId;
    @Column(name = "semester_id", nullable = true)
    private UUID semesterId;

    @Column(name = "time_table_type", nullable = true)
    @Enumerated(EnumType.STRING)
    private TimeTableType timeTableType;

    @Column(name = "exam_type", nullable = true)
    @Enumerated(EnumType.STRING)
    private ExamType examType;

    @Column(name = "exam_date", nullable = true)
    private Date exameDate;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
