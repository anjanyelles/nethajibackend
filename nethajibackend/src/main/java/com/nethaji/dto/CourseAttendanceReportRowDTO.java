package com.nethaji.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CourseAttendanceReportRowDTO {
    private UUID studentId;
    private String studentName;
    private String enrollmentNumber;

    private UUID courseId;
    private String courseName;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long totalClasses;
    private Long presentCount;
    private Long absentCount;
    private Long lateCount;
    private Long excusedCount;
    private Long notMarkedCount;

    private Double attendancePercentage;
    private Boolean lowAttendance;
}
