package com.nethaji.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class LecturerInfo {


    private UUID lecturerId;
    private String firstName;
    private String lastName;
    private String email;

    private int assistantCount;
    private int courseCount;

    private UUID lectureId;


    private Date createdAt;


    private String subjectType;

    private String graduationLecturer;


    private List<AssistantInfo> assistants = new ArrayList<>();
    private List<CourseInfo> courses = new ArrayList<>();

}
