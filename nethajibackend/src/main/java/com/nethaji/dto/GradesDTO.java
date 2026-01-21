package com.nethaji.dto;

import com.nethaji.Enums.Grade;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class GradesDTO {
    private UUID id;
    private UUID studentId;
    private UUID courseId;
    private UUID semesterId;
    private BigDecimal internalMarks;
    private BigDecimal midtermMarks;
    private BigDecimal finalMarks;
    private BigDecimal totalMarks;
    private Grade grade;
    private BigDecimal gradePoint;
    private BigDecimal percentage;
    private String studentName;
    private String courseName;
    private String semesterName;
}

