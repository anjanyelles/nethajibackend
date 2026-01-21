package com.nethaji.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class TranscriptDTO {
    private UUID studentId;
    private String studentName;
    private String enrollmentNumber;
    private UUID semesterId;
    private String semesterName;
    private List<CourseGradeDTO> courses;
    private BigDecimal sgpa;
    private BigDecimal cgpa;

    @Data
    public static class CourseGradeDTO {
        private String courseName;
        private String courseCode;
        private BigDecimal totalMarks;
        private String grade;
        private BigDecimal gradePoint;
        private Integer credits;
    }
}

