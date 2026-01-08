package com.nethaji.dto;

import com.nethaji.Enums.CourseType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class CourseListInfo {


    private UUID id;
    private String name;
    private String courseCode;
    private String description;
    private CourseType courseType;
    private Integer credits;
    private Boolean isElective;
    private Boolean isActive;
    private String syllabusPdfUrl;



    private Date createdAt;
    private Date updatedAt;





}
