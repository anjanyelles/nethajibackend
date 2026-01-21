package com.nethaji.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StudentRosterDTO {
    private UUID studentId;
    private String firstName;
    private String lastName;
    private String enrollmentNumber;
    private String email;
    private String sectionName;
}
