package com.nethaji.dto;

import com.nethaji.Enums.ExamType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class BulkMarksDTO {
    private UUID courseId;
    private ExamType examType;
    private BigDecimal maxMarks;
    private LocalDate examDate;
    private UUID evaluatedBy;
    private List<StudentMarksEntry> students;

    @Data
    public static class StudentMarksEntry {
        private UUID studentId;
        private BigDecimal marksObtained;
        private String remarks;
    }
}

