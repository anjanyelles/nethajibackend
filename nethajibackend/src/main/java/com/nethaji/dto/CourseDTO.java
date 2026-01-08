package com.nethaji.dto;

import com.nethaji.Enums.CourseType;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CourseDTO {
    private UUID id;


    private String name;
    private String courseCode;
    private String description;
    private Integer credits;
    private Integer semester;
    private UUID departmentSemesterId;
    private Boolean isElective;
    private Boolean isActive;
    private String syllabusPdfUrl;
    private CourseType courseType;


    private String departmentName;
    private String departmentCode;
    private String programName;
    private String programCode;


    private Date createdAt;
    private Date updatedAt;



    private Boolean status;
    private String message;
}

