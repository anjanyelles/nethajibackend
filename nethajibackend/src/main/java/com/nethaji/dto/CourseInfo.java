package com.nethaji.dto;


import lombok.Data;

import java.util.UUID;

@Data

public class CourseInfo {

    private UUID courseId;
    private String name;
    private String courseCode;
    private Integer semester;

}
