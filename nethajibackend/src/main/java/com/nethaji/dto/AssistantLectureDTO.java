package com.nethaji.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AssistantLectureDTO {
    private UUID assistantLectureId;
    private String assistantLectureName;
    private String assistantLectureEmail;
    private String assistantLectureMobile;
    private Boolean assistantLectureStatus;

    private List<LectureCoursesDTO> assistantCourses;
}

