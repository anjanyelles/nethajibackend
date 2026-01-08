package com.nethaji.dto;

import lombok.Data;

import java.util.UUID;


@Data
public class AssistantInfo {


    private UUID assistantId;
    private String firstName;
    private String lastName;
    private String email;

}
