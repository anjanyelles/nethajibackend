package com.nethaji.dto;

import com.nethaji.Enums.SubjectType;
import com.nethaji.entity.LecturerDetails;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateLecturerCourseAssignmentRequest {
    private UUID lecturerId;
    private UUID courseId;
    private LecturerDetails.AssignmentType assignmentType;
    private SubjectType subjectType;
    private Boolean isActive;
}
