package com.nethaji.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class InboxListItem {
    private UUID id;
    private String title;
    private String body;
    private Date createdAt;
    private Date readAt;
}
