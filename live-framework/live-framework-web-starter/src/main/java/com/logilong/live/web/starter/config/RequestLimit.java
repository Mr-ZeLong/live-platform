package com.logilong.live.web.starter.config;

import java.lang.annotation.*;

/**
 * 拦截重复请求注解，限流组件实现
 */

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

    /**
     * 允许请求的量
     */
    int limit();

    /**
     * 指定时间范围，单位秒
     */
    int second();

    /**
     * 如果出现了拦截，那么就按照msg文案进行提示
     */
    String msg() default "请求过于频繁";
}
