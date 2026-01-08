package com.nethaji.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class LecturerWithAssistantsResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String userType;
    private List<AssistantLecturerResponse> assistantLecturers;
    private int totalAssistantLecturers;
}