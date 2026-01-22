package com.nethaji.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AttendanceViewRowDTO {
    private UUID studentId;
    private String studentName;
    private String enrollmentNumber;

    private UUID courseId;
    private String courseName;

    private LocalDate attendanceDate;
    private String status;

    private UUID attendanceId;
    private String remarks;
}
