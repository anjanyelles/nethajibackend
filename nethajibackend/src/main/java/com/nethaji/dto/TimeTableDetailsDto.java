package com.nethaji.dto;

import com.nethaji.Enums.ExamType;
import com.nethaji.Enums.TimeTableType;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TimeTableDetailsDto {
    private UUID id;
    private UUID departmentId;
    private UUID semesterId;
    private String subjectName;
    private String dayOfMonth;
    private Integer periodTime;
    private String periodStartToEnd;
    private Date createdAt;

    private TimeTableType timeTableType;
    private String examTime;

    private String examSubject;

    private String examCenter;

    private ExamType examType;

    private Date exameDate;

    private Date holidaysStartDate;

    private String holidayType;

    private Date holidaysEndDate;

    private String status;

}
