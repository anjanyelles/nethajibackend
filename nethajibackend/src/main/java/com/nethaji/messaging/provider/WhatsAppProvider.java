package com.nethaji.messaging.provider;

public interface WhatsAppProvider {
    WhatsAppSendResult sendTemplate(String toMobileE164, String templateName, String languageCode, String bodyText);
}
