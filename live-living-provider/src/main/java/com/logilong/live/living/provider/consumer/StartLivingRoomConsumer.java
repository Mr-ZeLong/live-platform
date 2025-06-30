package com.logilong.live.living.provider.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.logilong.live.common.interfaces.topic.LivingProviderTopicNames;
import com.logilong.live.sku.interfaces.ISkuStockInfoRPC;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import com.logilong.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StartLivingRoomConsumer implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartLivingRoomConsumer.class);
    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @DubboReference
    ISkuStockInfoRPC skuStockInfoRPC;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + StartLivingRoomConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //监听礼物缓存数据更新的行为
        mqPushConsumer.subscribe(LivingProviderTopicNames.START_LIVING_ROOM, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                JSONObject jsonObject = JSON.parseObject(new String(msg.getBody()));
                Long anchorId = jsonObject.getLong("anchorId");
                skuStockInfoRPC.prepareStockInfo(anchorId);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("mq消费者启动成功,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
