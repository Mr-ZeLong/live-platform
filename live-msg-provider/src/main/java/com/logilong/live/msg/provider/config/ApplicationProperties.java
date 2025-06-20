package com.logilong.live.msg.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "sms.ccp")
@Configuration
@Data
public class ApplicationProperties {

    private String smsServerIp;
    private Integer port;
    private String accountSId;
    private String accountToken;
    private String appId;
    private String testPhone;

}
