package com.nethaji.messaging.provider;

public interface EmailProvider {
    EmailSendResult send(String to, String subject, String body);
}
