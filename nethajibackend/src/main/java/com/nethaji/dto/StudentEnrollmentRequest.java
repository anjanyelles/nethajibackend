package com.nethaji.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class StudentEnrollmentRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String hallTicketNo;

    private Date dateOfBirth;

    private String interGroupCollege;

    private String aadharNo;

    @NotBlank(message = "Aadhar mobile is required")
    private String aadharMobile;

    private String whatsappNo;

    private String address;

    private String village;

    private List<String> courses;

    private String referredBy;
}
