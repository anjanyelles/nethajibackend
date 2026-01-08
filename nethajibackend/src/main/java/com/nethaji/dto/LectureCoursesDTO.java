package com.nethaji.dto;


import com.nethaji.Enums.CourseType;
import com.nethaji.entity.LecturerDetails;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class LectureCoursesDTO {

    private UUID id;
    private UUID lectureId;
    private  UUID assistantLectureId;

    private UUID courseId;
    private String courseName;
    private String courseCode;
    private String description;
    private Integer credits;
    private Integer semester;
    private UUID programId;
    private UUID departmentId;
    private Boolean isElective;
    private Boolean isActive;
    private String syllabusPdfUrl;
    private CourseType courseType;

    private LecturerDetails.AssignmentType assignmentType;
    private Boolean lectureCourseStatus;

    private Date createdAt;
    private Date updatedAt;

}
