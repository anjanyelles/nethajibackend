package com.nethaji.messaging.provider;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SesEmailProvider implements EmailProvider {

    private final AmazonSimpleEmailService sesClient;

    @Value("${messaging.email.from:admin@services.asoft.click}")
    private String fromEmail;

    public SesEmailProvider(
            @Value("${aws.accesskey:}") String accessKey,
            @Value("${aws.secretkey:}") String secretKey,
            @Value("${messaging.email.region:AP_SOUTH_1}") String region
    ) {
        boolean hasStatic = accessKey != null && !accessKey.isBlank() && secretKey != null && !secretKey.isBlank();

        AmazonSimpleEmailServiceClientBuilder builder = AmazonSimpleEmailServiceClientBuilder.standard();

        // Region is configurable, but keep current default AP_SOUTH_1.
        Regions awsRegion;
        try {
            String normalized = region == null ? "" : region.trim();
            normalized = normalized.replace('_', '-').toLowerCase();
            awsRegion = Regions.fromName(normalized);
        } catch (Exception ignored) {
            awsRegion = Regions.AP_SOUTH_1;
        }
        builder = builder.withRegion(awsRegion);

        if (hasStatic) {
            builder = builder.withCredentials(
                    new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey.trim(), secretKey.trim()))
            );
        } else {
            builder = builder.withCredentials(DefaultAWSCredentialsProviderChain.getInstance());
        }

        this.sesClient = builder.build();
    }

    @Override
    public EmailSendResult send(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            return new EmailSendResult(false, null, "INVALID_TO", "Recipient email is empty");
        }
        try {
            SendEmailRequest req = new SendEmailRequest()
                    .withSource(fromEmail)
                    .withDestination(new Destination().withToAddresses(to))
                    .withMessage(
                            new Message()
                                    .withSubject(new Content().withCharset("UTF-8").withData(subject == null ? "" : subject))
                                    .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(body == null ? "" : body)))
                    );

            SendEmailResult res = sesClient.sendEmail(req);
            return new EmailSendResult(true, res.getMessageId(), null, null);
        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? "SES error" : ex.getMessage();
            if (msg.toLowerCase().contains("invalidclienttokenid") || msg.toLowerCase().contains("security token")) {
                return new EmailSendResult(false, null, "AWS_CREDENTIALS_INVALID", msg);
            }
            return new EmailSendResult(false, null, "SES_ERROR", msg);
        }
    }
}
