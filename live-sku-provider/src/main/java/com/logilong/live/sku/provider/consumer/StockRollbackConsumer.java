package com.logilong.live.sku.provider.consumer;

import com.alibaba.fastjson.JSON;
import com.logilong.live.common.interfaces.topic.SkuProviderTopicNames;
import com.logilong.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import com.logilong.live.sku.dto.RollbackStockInfoDTO;
import com.logilong.live.sku.provider.service.ISkuStockInfoService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * 在容器初始化的时候，消费关于库存回滚的主题消息
 */
@Configuration
public class StockRollbackConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockRollbackConsumer.class);

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private ISkuStockInfoService skuStockInfoService;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        //老版本中会开启，新版本的mq不需要使用到
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + StockRollbackConsumer.class.getSimpleName());
        //一次从broker中拉取10条消息到本地内存当中进行消费
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //监听礼物缓存数据更新的行为
        mqPushConsumer.subscribe(SkuProviderTopicNames.ROLL_BACK_STOCK, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            RollbackStockInfoDTO rollbackStockInfoDTO = JSON.parseObject(new String(msgs.get(0).getBody()), RollbackStockInfoDTO.class);
            skuStockInfoService.stockRollbackHandler(rollbackStockInfoDTO);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("mq消费者启动成功,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
