package com.nethaji.dto;


import lombok.Data;

@Data
public class StudentLoginRequest {


    private String email;
    private String  enrollmentNumber;
    private String password;

}
