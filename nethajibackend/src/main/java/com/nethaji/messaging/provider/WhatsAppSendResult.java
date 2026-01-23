package com.nethaji.messaging.provider;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WhatsAppSendResult {
    private boolean accepted;
    private String providerMessageId;
    private String errorCode;
    private String errorMessage;
}
