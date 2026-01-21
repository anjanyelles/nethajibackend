package com.nethaji.dto;

import com.nethaji.Enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class BulkAttendanceDTO {
    private UUID courseId;
    private LocalDate attendanceDate;
    private UUID markedBy;
    private List<StudentAttendanceEntry> students;

    @Data
    public static class StudentAttendanceEntry {
        private UUID studentId;
        private AttendanceStatus status;
        private String remarks;
    }
}

