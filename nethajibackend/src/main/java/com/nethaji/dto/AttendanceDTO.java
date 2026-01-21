package com.nethaji.dto;

import com.nethaji.Enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AttendanceDTO {
    private UUID id;
    private UUID studentId;
    private UUID courseId;
    private LocalDate attendanceDate;
    private AttendanceStatus status;
    private UUID markedBy;
    private String remarks;
    private String studentName;
    private String courseName;
}

