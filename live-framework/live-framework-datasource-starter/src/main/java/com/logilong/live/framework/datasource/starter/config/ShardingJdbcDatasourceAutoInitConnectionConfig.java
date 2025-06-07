package com.logilong.live.framework.datasource.starter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * HikariDataSource 存在bug:
 * 没法在application初始化的时候根据参数进行初始化
 */

@Configuration
public class ShardingJdbcDatasourceAutoInitConnectionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardingJdbcDatasourceAutoInitConnectionConfig.class);
    @Bean
    public ApplicationRunner runner(DataSource dataSource){
        return args -> {
            LOGGER.info("dataSource: {}", dataSource);
            // 手动触发触发连接池的连接创建
            Connection connection = dataSource.getConnection();
        };
    }
}
