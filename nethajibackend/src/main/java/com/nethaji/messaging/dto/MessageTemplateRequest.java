package com.nethaji.messaging.dto;

import lombok.Data;

@Data
public class MessageTemplateRequest {
    private String code;
    private String name;
    private String description;
    private String defaultSubject;
    private String defaultBody;
}
