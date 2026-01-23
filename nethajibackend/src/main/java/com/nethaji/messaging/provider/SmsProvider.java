package com.nethaji.messaging.provider;

public interface SmsProvider {
    SmsSendResult send(String toMobile, String message);
}
