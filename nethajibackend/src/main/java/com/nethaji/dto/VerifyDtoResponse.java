package com.nethaji.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class VerifyDtoResponse {
    private String timeInMilliSeconds;

    private String emailOtpSession;

    private String whatsappOtpSession;

    private String salt;

    private String status;

    private String token;

    private String accessToken;

    private UUID userId;

    private String refreshToke;

    private UUID id;

    private boolean userStatus;

    private String userRole;



}

