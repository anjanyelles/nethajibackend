package com.nethaji.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AssignmentDTO {
    private UUID id;
    private UUID courseId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private BigDecimal maxMarks;
    private UUID createdBy;
    private String fileUrl;
    private Boolean isActive;
    private String courseName;
    private String createdByName;
}

