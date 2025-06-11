package com.logilong.live.id.generate.provider;

import com.logilong.live.id.generate.enums.IdTypeEnum;
import com.logilong.live.id.generate.provider.service.IdGenerateService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.HashSet;

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class IdGenerateApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(IdGenerateApplication.class);

    @Resource
    private IdGenerateService idGenerateService;
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        HashSet<Long> idSet = new HashSet<>();
        for(int i = 0; i < 600; i++){
            Long seqId = idGenerateService.getSeqId(IdTypeEnum.USER_ID.getCode());
            idSet.add(seqId);
        }
        log.info("idSet.size:{}", idSet.size());
    }
}
