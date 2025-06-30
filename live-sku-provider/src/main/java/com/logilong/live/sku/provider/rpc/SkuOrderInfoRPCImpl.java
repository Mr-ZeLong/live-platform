package com.logilong.live.sku.provider.rpc;

import com.alibaba.fastjson.JSON;
import com.logilong.live.common.interfaces.topic.SkuProviderTopicNames;
import com.logilong.live.sku.constants.SkuOrderInfoEnum;
import com.logilong.live.sku.dto.*;
import com.logilong.live.sku.interfaces.ISkuOrderInfoRPC;
import com.logilong.live.sku.provider.dao.po.SkuInfoPO;
import com.logilong.live.sku.provider.dao.po.SkuOrderInfoPO;
import com.logilong.live.sku.provider.service.IShopCarService;
import com.logilong.live.sku.provider.service.ISkuInfoService;
import com.logilong.live.sku.provider.service.ISkuOrderInfoService;
import com.logilong.live.sku.provider.service.ISkuStockInfoService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.bank.interfaces.ILiveCurrencyAccountRPC;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@DubboService
public class SkuOrderInfoRPCImpl implements ISkuOrderInfoRPC {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SkuOrderInfoRPCImpl.class);
    @Resource
    private ISkuOrderInfoService skuOrderInfoService;
    @Resource
    private IShopCarService shopCarService;
    @Resource
    private ISkuStockInfoService skuStockInfoService;
    @Resource
    private ISkuInfoService skuInfoService;
    @Resource
    private ILiveCurrencyAccountRPC liveCurrencyAccountRPC;
    @Resource
    private MQProducer mqProducer;


    @Override
    public SkuOrderInfoRespDTO queryByUserIdAndRoomId(Long userId, Integer roomId) {
        return skuOrderInfoService.queryByUserIdAndRoomId(userId, roomId);
    }

    @Override
    public boolean insertOne(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        return skuOrderInfoService.insertOne(skuOrderInfoReqDTO) != null;
    }

    @Override
    public boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        return skuOrderInfoService.updateOrderStatus(skuOrderInfoReqDTO);
    }

    @Override
    public SkuPrepareOrderInfoDTO prepareOrder(PrepareOrderReqDTO reqDTO) {
        ShopCarReqDTO shopCarReqDTO = ConvertBeanUtils.convert(reqDTO, ShopCarReqDTO.class);
        ShopCarRespDTO carInfo = shopCarService.getShopCarInfo(shopCarReqDTO);
        List<ShopCarItemRespDTO> shopCarItemRespDTOList = carInfo.getShopCarItemRespDTOList();
        if (CollectionUtils.isEmpty(shopCarItemRespDTOList)) {
            return new SkuPrepareOrderInfoDTO();
        }
        List<Long> skuIdList = shopCarItemRespDTOList.stream().map(item -> item.getSkuInfoDTO().getSkuId()).collect(Collectors.toList());
        // 进行商品库存的扣减
        //核心的知识点库存回滚
        //10个skuId前5个扣减成功了，后边5个有问题
        boolean isDecrSuccess = skuStockInfoService.decrStockNumBySkuIdsCache(skuIdList, 1);
        if (!isDecrSuccess) {
            return null;
        }
        //订单超时的概念，21：00,21：30分订单会自动关闭，21：25分的时候会有订单是醒功能
        //1.定时任务 扫描DB, 指定好索引，如果数据量非常高，扫描表的sqL会很耗时
        //2.redis的过期回调key过期之后，会有一个回调通知，ttl到期之后会回调到订阅方，回调并不是高可靠的，可能回丢失
        //3.rocketmq延迟消息，时间轮去做的，将扣减库存的信息利用下mg发送出去，在延迟回调处进行校验，
        //4.将扣减库存的信息，利用mq发送出去，在回调处进行校验
        SkuOrderInfoReqDTO skuOrderInfoReqDTO = new SkuOrderInfoReqDTO();
        skuOrderInfoReqDTO.setSkuIdList(skuIdList);
        skuOrderInfoReqDTO.setUserId(reqDTO.getUserId());
        skuOrderInfoReqDTO.setRoomId(reqDTO.getRoomId());
        skuOrderInfoReqDTO.setStatus(SkuOrderInfoEnum.PREPARE_PAY.getCode());
        SkuOrderInfoPO skuOrderInfo = skuOrderInfoService.insertOne(skuOrderInfoReqDTO);
        if (skuOrderInfo == null) {
            return null;
        }
        Long orderId = skuOrderInfo.getId();
        Long userId = reqDTO.getUserId();
        //发送延时MQ：若订单未支付，进行库存回滚
        stockRollbackHandler(userId, orderId);

        List<SkuPrepareOrderItemInfoDTO> skuPrepareOrderItemInfoDTOList = new ArrayList<>();
        int totalPrice = 0;
        for (ShopCarItemRespDTO shopCarItemRespDTO : shopCarItemRespDTOList) {
            SkuPrepareOrderItemInfoDTO skuPrepareOrderItemInfoDTO = new SkuPrepareOrderItemInfoDTO();
            skuPrepareOrderItemInfoDTO.setSkuInfoDTO(shopCarItemRespDTO.getSkuInfoDTO());
            skuPrepareOrderItemInfoDTO.setCount(shopCarItemRespDTO.getCount());
            totalPrice += shopCarItemRespDTO.getSkuInfoDTO().getSkuPrice();
            skuPrepareOrderItemInfoDTOList.add(skuPrepareOrderItemInfoDTO);
        }

        SkuPrepareOrderInfoDTO skuPrepareOrderInfoDTO = new SkuPrepareOrderInfoDTO();
        skuPrepareOrderInfoDTO.setTotalPrice(totalPrice);
        skuPrepareOrderInfoDTO.setSkuPrepareOrderItemInfoDTOList(skuPrepareOrderItemInfoDTOList);
        return skuPrepareOrderInfoDTO;
    }

    @Override
    public boolean payNow(PayNowReqDTO payNowReqDTO) {
        SkuOrderInfoRespDTO skuOrderInfoRespDTO = skuOrderInfoService.queryByUserIdAndRoomId(payNowReqDTO.getUserId(), payNowReqDTO.getRoomId());
        if (SkuOrderInfoEnum.PREPARE_PAY.getCode() != skuOrderInfoRespDTO.getStatus()) {
            LOGGER.error("payNow 订单状态为：{}，不是待支付状态", skuOrderInfoRespDTO.getStatus());
            return false;
        }
        List<Long> skuIdList = Arrays.stream(skuOrderInfoRespDTO.getSkuIdList().split(",")).toList().stream().map(Long::valueOf).toList();
        List<SkuInfoPO> skuInfoList = skuInfoService.queryBySkuIds(skuIdList);
        int num = 0;
        for (SkuInfoPO skuInfo : skuInfoList) {
            num += skuInfo.getSkuPrice();
        }
        Integer balance = liveCurrencyAccountRPC.getBalance(payNowReqDTO.getUserId());
        if (balance - num < 0) {
            LOGGER.error("payNow balance is no enough! balance = {}, num = {}", balance, num);
            return false;
        }

        SkuOrderInfoReqDTO skuOrderInfoReqDTO = new SkuOrderInfoReqDTO();
        skuOrderInfoReqDTO.setOrderId(skuOrderInfoRespDTO.getId());
        skuOrderInfoReqDTO.setStatus(SkuOrderInfoEnum.PAYED.getCode());
        skuOrderInfoReqDTO.setUserId(skuOrderInfoRespDTO.getUserId());
        skuOrderInfoReqDTO.setRoomId(skuOrderInfoRespDTO.getRoomId());

        //扣减虚拟币
        boolean isSuccess = liveCurrencyAccountRPC.decrV2(skuOrderInfoRespDTO.getUserId(), num);
        if (!isSuccess) {
            LOGGER.error("payNow accountRpc.decrV2() isSuccess: {}", isSuccess);
            return false;
        }
        //更新订单状态
        isSuccess = skuOrderInfoService.updateOrderStatus(skuOrderInfoReqDTO);
        if (!isSuccess) {
            LOGGER.error("payNow skuOrderInfoService.updateStatus() isSuccess: {}", isSuccess);
            return false;
        }
        //清空购物车
        ShopCarReqDTO shopCarReqDTO = new ShopCarReqDTO();
        shopCarReqDTO.setUserId(skuOrderInfoReqDTO.getUserId());
        shopCarReqDTO.setRoomId(skuOrderInfoReqDTO.getRoomId());
        isSuccess = shopCarService.clearShopCar(shopCarReqDTO);
        if (!isSuccess) {
            LOGGER.error("payNow shopCarService.clearShopCar() isSuccess: {}", isSuccess);
        }
        return true;
    }

    /**
     * 库存回滚的mq延迟消息发送
     */
    private void stockRollbackHandler(Long userId, Long orderId) {
        Message message = new Message();
        message.setTopic(SkuProviderTopicNames.ROLL_BACK_STOCK);
        RollbackStockInfoDTO rollbackStockInfoDTO = new RollbackStockInfoDTO();
        rollbackStockInfoDTO.setUserId(userId);
        rollbackStockInfoDTO.setOrderId(orderId);
        message.setBody(JSON.toJSONString(rollbackStockInfoDTO).getBytes());
        //delayTimeLevel=1s 5s 10s(3) 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m(16) 1h 2h
        //从1开始 16 就是延迟 30m
        message.setDelayTimeLevel(16);
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
