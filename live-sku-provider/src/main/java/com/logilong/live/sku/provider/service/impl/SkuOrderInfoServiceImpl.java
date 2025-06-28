package com.logilong.live.sku.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.logilong.live.framework.redis.starter.key.SkuProviderCacheKeyBuilder;
import com.logilong.live.sku.dto.SkuOrderInfoReqDTO;
import com.logilong.live.sku.dto.SkuOrderInfoRespDTO;
import com.logilong.live.sku.provider.dao.mapper.ISkuOrderInfoMapper;
import com.logilong.live.sku.provider.dao.po.SkuOrderInfoPO;
import com.logilong.live.sku.provider.service.ISkuOrderInfoService;
import jakarta.annotation.Resource;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class SkuOrderInfoServiceImpl implements ISkuOrderInfoService {
    
    @Resource
    private ISkuOrderInfoMapper skuOrderInfoMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SkuProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public SkuOrderInfoRespDTO queryByUserIdAndRoomId(Long userId, Integer roomId) {
        String cacheKey = cacheKeyBuilder.buildSkuOrderInfo(userId, roomId);
        Object object = redisTemplate.opsForValue().get(cacheKey);
        if (object != null) {
            return ConvertBeanUtils.convert(object, SkuOrderInfoRespDTO.class);
        }

        LambdaQueryWrapper<SkuOrderInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuOrderInfoPO::getUserId, userId);
        queryWrapper.eq(SkuOrderInfoPO::getRoomId, roomId);
        queryWrapper.orderByDesc(SkuOrderInfoPO::getId);
        queryWrapper.last("limit 1");

        SkuOrderInfoPO skuOrderInfoPO = skuOrderInfoMapper.selectOne(queryWrapper);
        if(skuOrderInfoPO == null) return null;

        SkuOrderInfoRespDTO skuOrderInfoRespDTO = ConvertBeanUtils.convert(skuOrderInfoPO, SkuOrderInfoRespDTO.class);
        redisTemplate.opsForValue().set(cacheKey, skuOrderInfoRespDTO, 1, TimeUnit.HOURS);
        return skuOrderInfoRespDTO;
    }

    @Override
    public SkuOrderInfoRespDTO queryByOrderId(Long orderId) {
        String cacheKey = cacheKeyBuilder.buildSkuOrderInfoByOrderId(orderId);

        Object object = redisTemplate.opsForValue().get(cacheKey);
        if (object != null) {
            return ConvertBeanUtils.convert(object, SkuOrderInfoRespDTO.class);
        }
        SkuOrderInfoPO skuOrderInfoPO = skuOrderInfoMapper.selectById(orderId);
        if (skuOrderInfoPO == null) {
            return null;
        }
        SkuOrderInfoRespDTO skuOrderInfoRespDTO = ConvertBeanUtils.convert(skuOrderInfoPO, SkuOrderInfoRespDTO.class);
        redisTemplate.opsForValue().set(cacheKey, skuOrderInfoRespDTO, 1, TimeUnit.HOURS);
        return skuOrderInfoRespDTO;
    }

    @Override
    public SkuOrderInfoPO insertOne(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        // hutool工具包的StrUtil
        String skuIdListStr = StrUtil.join(",", skuOrderInfoReqDTO.getSkuIdList());
        SkuOrderInfoPO skuOrderInfoPO = ConvertBeanUtils.convert(skuOrderInfoReqDTO, SkuOrderInfoPO.class);
        skuOrderInfoPO.setSkuIdList(skuIdListStr);
        skuOrderInfoMapper.insert(skuOrderInfoPO);
        return skuOrderInfoPO;
    }

    @Override
    public boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        SkuOrderInfoPO skuOrderInfoPO = new SkuOrderInfoPO();
        skuOrderInfoPO.setStatus(skuOrderInfoReqDTO.getStatus());
        skuOrderInfoPO.setId(skuOrderInfoReqDTO.getId());
        skuOrderInfoMapper.updateById(skuOrderInfoPO);
        // 订单状态更新后删除旧缓存
        String cacheKey = cacheKeyBuilder.buildSkuOrderInfo(skuOrderInfoReqDTO.getUserId(), skuOrderInfoReqDTO.getRoomId());
        redisTemplate.delete(cacheKey);
        return false;
    }
}
