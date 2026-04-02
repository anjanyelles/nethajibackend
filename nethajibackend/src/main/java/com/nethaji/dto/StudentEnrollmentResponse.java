package com.nethaji.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class StudentEnrollmentResponse {

    private UUID id;
    private String email;
    private String fullName;
    private String hallTicketNo;
    private Date dateOfBirth;
    private String interGroupCollege;
    private String aadharNo;
    private String aadharMobile;
    private String whatsappNo;
    private String address;
    private String village;
    private List<String> courses;
    private String referredBy;
    private Date createdAt;
    private Date updatedAt;
}
