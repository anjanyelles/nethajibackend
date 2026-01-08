package com.nethaji.dto;


import lombok.Data;

import java.util.UUID;

@Data

public class LecturerResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String userType;
}

