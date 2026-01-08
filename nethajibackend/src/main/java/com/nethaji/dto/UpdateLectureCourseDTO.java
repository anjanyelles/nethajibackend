package com.nethaji.dto;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateLectureCourseDTO {

    private UUID lectureId;

    private List<LectureCoursesDTO> oldCourseIds;
    private List<UUID> newCourseIds;

}
