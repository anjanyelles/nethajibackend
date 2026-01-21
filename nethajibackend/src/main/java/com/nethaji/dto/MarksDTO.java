package com.nethaji.dto;

import com.nethaji.Enums.ExamType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class MarksDTO {
    private UUID id;
    private UUID studentId;
    private UUID courseId;
    private ExamType examType;
    private BigDecimal marksObtained;
    private BigDecimal maxMarks;
    private LocalDate examDate;
    private UUID evaluatedBy;
    private String remarks;
    private String studentName;
    private String courseName;
}

