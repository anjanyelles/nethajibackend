package com.nethaji.dto;


import lombok.Data;

import java.util.UUID;

@Data

public class AssistantLecturerResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;



    private String userType;
    private String  lectureName;
    private UUID lectureId;
}
