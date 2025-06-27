package com.logilong.live.framework.redis.starter.key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * 若后续有其他的项目，则会有很多的keyBuilder，那么需要一个匹配器，来匹配当前项目所使用的keyBuilder
 * 这里只加载live项目的keyBuilder，其他项目的keyBuilder则不需要加载，避免浪费资源
 */

public class RedisKeyLoadMatch implements Condition {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisKeyLoadMatch.class);

    private static final String PREFIX = "live";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String appName = context.getEnvironment().getProperty("spring.application.name");
        if (appName == null) {
            LOGGER.error("没有匹配到应用名称，所以无法加载任何RedisKeyBuilder对象");
            return false;
        }
        try {
            Field classNameField = metadata.getClass().getDeclaredField("className");
            classNameField.setAccessible(true);
            String keyBuilderName = (String) classNameField.get(metadata);
            List<String> splitList = Arrays.asList(keyBuilderName.split("\\."));
            //忽略大小写，统一用live开头命名
            String classSimplyName = PREFIX + splitList.get(splitList.size() - 1).toLowerCase();
            boolean matchStatus = classSimplyName.contains(appName.replaceAll("-", ""));
            LOGGER.info("keyBuilderClass is {},matchStatus is {}", keyBuilderName, matchStatus);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}