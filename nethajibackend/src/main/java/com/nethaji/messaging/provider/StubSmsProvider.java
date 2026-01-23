package com.nethaji.messaging.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StubSmsProvider implements SmsProvider {

    private static final Logger log = LoggerFactory.getLogger(StubSmsProvider.class);

    @Value("${messaging.sms.enabled:false}")
    private boolean smsEnabled;

    @Override
    public SmsSendResult send(String toMobile, String message) {
        if (!smsEnabled) {
            log.info("SMS disabled. to={}, msg={}", toMobile, message);
            return new SmsSendResult(true, "SMS_DISABLED", null, null);
        }

        log.info("SMS (stub) to={}, msg={}", toMobile, message);
        return new SmsSendResult(true, "SMS_STUB", null, null);
    }
}
