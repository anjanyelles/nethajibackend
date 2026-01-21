package com.nethaji.dto;

import com.nethaji.Enums.Gender;
import com.nethaji.Enums.StudentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class StudentProfileDTO {


    private UUID id;
    private UUID userId;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String guardianContact;
    private String guardianName;
    private String guardianRelation;
    private String profilePictureUrl;
    private String bloodGroup;
    private String alternateContact;
    private BigDecimal tenthPercentage;
    private String tenthBoard;
    private Integer tenthYearOfPassing;
    private BigDecimal twelfthPercentage;
    private String twelfthBoard;
    private Integer twelfthYearOfPassing;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    private String enrollmentNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private Date userCreatedAt;



}

