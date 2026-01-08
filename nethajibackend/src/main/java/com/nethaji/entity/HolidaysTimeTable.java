package com.nethaji.entity;

import com.nethaji.Enums.TimeTableType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;


@Entity
@Data
@Table(name = "holiday_time_table")
public class HolidaysTimeTable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "holidays_start_date", nullable = true)
    private Date holidaysStartDate;

    @Column(name = "holiday_type", nullable = true)
    private String holidayType;

    @Column(name = "holidays_end_date", nullable = true)
    private Date holidaysEndDate;

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
