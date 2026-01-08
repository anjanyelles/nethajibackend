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
@Table(name = "time_table_details")
public class TimeTableDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "department_id", nullable = true)
    private UUID departmentId;
    @Column(name = "semester_id", nullable = true)
    private UUID semesterId;
    @Column(name = "subject_name", nullable = true)
    private String subjectName;
    @Column(name = "day_of_month", nullable = true)
    private String dayOfMonth;
    @Column(name = "period_time", nullable = true)
    private Integer periodTime;
    @Column(name = "period_start_to_end", nullable = true)
    private String periodStartToEnd;

    @Column(name = "time_table_type", nullable = true)
    @Enumerated(EnumType.STRING)
    private TimeTableType timeTableType;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
