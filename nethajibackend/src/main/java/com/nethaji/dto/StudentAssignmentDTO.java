package com.nethaji.dto;

import com.nethaji.Enums.AssignmentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class StudentAssignmentDTO {
    private UUID id;
    private UUID assignmentId;
    private UUID studentId;
    private LocalDate submissionDate;
    private String fileUrl;
    private BigDecimal marksObtained;
    private String feedback;
    private AssignmentStatus status;
    private UUID gradedBy;
    private String studentName;
    private String assignmentTitle;
    private LocalDate dueDate;
    private BigDecimal maxMarks;
}

