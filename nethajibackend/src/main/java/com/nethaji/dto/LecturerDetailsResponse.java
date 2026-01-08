
package com.nethaji.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class LecturerDetailsResponse {
    private UUID lecturerId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String userType;
    private List<CourseInfo> courses;
    private int totalCourses;
    private int totalAssistantLecturers;
}