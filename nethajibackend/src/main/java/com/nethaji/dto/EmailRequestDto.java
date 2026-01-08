package com.nethaji.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmailRequestDto {

    private List<String> emails;
    private TemplateContext templateContext;
    private String subject;
    private String senderName;
    private String fromMail;
    private String templateName;

}
