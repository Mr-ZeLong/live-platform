package com.logilong.live.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 不添加该配置类，报错：A component required a bean of type 'org.springframework.web.client.RestTemplate' that could not be found.
 */

@Configuration
public class RestTemplateConfig {

   @Bean
   public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
       return new RestTemplate(factory);
   }

   @Bean
   public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
       SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
       factory.setReadTimeout(150000); // ms
       factory.setConnectTimeout(150000); // ms
       return factory;
   }
}

