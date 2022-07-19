package com.omao.smsOTP.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Data // this annotation will generate getters and setters for us
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String trialNumber;

}
