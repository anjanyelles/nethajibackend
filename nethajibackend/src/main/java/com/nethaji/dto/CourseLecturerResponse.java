package com.nethaji.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CourseLecturerResponse {
    private UUID courseId;
    private String courseName;
    private List<LecturerWithAssistantsResponse> lecturers;
    private int totalLecturers;
    private int totalAssistantLecturers;
}
