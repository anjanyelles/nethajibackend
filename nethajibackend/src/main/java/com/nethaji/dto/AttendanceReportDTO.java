package com.nethaji.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AttendanceReportDTO {
    private UUID studentId;
    private UUID courseId;
    private String studentName;
    private String courseName;
    private Long totalClasses;
    private Long presentCount;
    private Long absentCount;
    private Long lateCount;
    private Long excusedCount;
    private Double attendancePercentage;
}

