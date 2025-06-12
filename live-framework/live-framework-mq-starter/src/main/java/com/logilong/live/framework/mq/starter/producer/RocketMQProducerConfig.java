package com.logilong.live.framework.mq.starter.producer;

import jakarta.annotation.Resource;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import com.logilong.live.framework.mq.starter.properties.RocketMQProducerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
public class RocketMQProducerConfig {

    private static final Logger log = LoggerFactory.getLogger(RocketMQProducerConfig.class);

    @Resource
    private RocketMQProducerProperties rocketMQProducerProperties;


    @Bean
    public ThreadPoolExecutor rocketMQAsyncThreadPool() {
        return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 100, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable run) {
                return new Thread(run, "rocketmq-async-thread-" + counter.incrementAndGet());
            }
        });
    }

    @Bean
    public MQProducer mqProducer(ThreadPoolExecutor rocketMQAsyncThreadPool) {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setProducerGroup(rocketMQProducerProperties.getGroupName());
        defaultMQProducer.setNamesrvAddr(rocketMQProducerProperties.getNameSrv());
        defaultMQProducer.setRetryTimesWhenSendFailed(rocketMQProducerProperties.getRetryTimes());
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(rocketMQProducerProperties.getRetryTimes());
        defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
        defaultMQProducer.setAsyncSenderExecutor(rocketMQAsyncThreadPool);
        try {
            defaultMQProducer.start();
            log.info("=============== mq生产者启动成功,namesrv is {} ==================", rocketMQProducerProperties.getNameSrv());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        return defaultMQProducer;
    }
}
