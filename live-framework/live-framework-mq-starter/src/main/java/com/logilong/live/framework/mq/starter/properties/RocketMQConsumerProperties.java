package com.logilong.live.framework.mq.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "rmq.consumer")
@Configuration
@Data
public class RocketMQConsumerProperties {

    private String nameSrv;
    private String groupName;
}
