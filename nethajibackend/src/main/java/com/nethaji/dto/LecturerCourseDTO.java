package com.nethaji.dto;

import com.nethaji.Enums.CourseType;
import com.nethaji.entity.LecturerDetails;
import lombok.Data;

import java.util.UUID;

@Data
public class LecturerCourseDTO {
    private UUID courseId;
    private String courseName;
    private String courseCode;
    private CourseType courseType;

    private UUID departmentSemesterId;
    private Integer semester;

    private UUID departmentId;
    private String departmentName;
    private String departmentCode;

    private UUID programId;
    private String programName;
    private String programCode;

    private LecturerDetails.AssignmentType assignmentType;
}
