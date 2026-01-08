package com.nethaji.dto;


import com.nethaji.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class EmailOtpRequest {

    private String password;

    private String timeInMilliSeconds;

    private String emailOtpSession;

    private String emailOtp;

    private String salt;

    private String primaryType;

    private String email;
    private String passwordHash;
    private User.UserType userType;
    private String firstName;
    private String lastName;

    private Date createdAt;

    private Date updatedAt;
    private Date lastLogin;

    private String mobileNumber;


}


