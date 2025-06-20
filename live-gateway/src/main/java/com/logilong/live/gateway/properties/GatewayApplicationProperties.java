package com.logilong.live.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "gateway")
@Configuration
@RefreshScope
@Data
public class GatewayApplicationProperties {

    private List<String> notCheckUrlList;
}
