package com.nethaji.dto;

import com.nethaji.Enums.Branch;
import com.nethaji.Enums.SubjectType;
import com.nethaji.entity.User;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RegisterRequest {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String password;

    private Branch branch;

    private String countryCode;

    private User.UserType userType;

    private String  graduationType;
    private SubjectType subjectType;
    private UUID supervisorId;
    private UUID programId;
    private boolean loginStatus;

    private Integer semester;
    private Boolean studentStatus;
    private String studentProgramName;
    private String  studentCurrentSemester;
    private String  enrollmentNumber;


}
