package com.logilong.live.sku.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.logilong.live.framework.redis.starter.key.SkuProviderCacheKeyBuilder;
import com.logilong.live.sku.provider.dao.mapper.ISkuInfoMapper;
import com.logilong.live.sku.provider.dao.po.SkuInfoPO;
import com.logilong.live.sku.provider.service.ISkuInfoService;
import jakarta.annotation.Resource;
import com.logilong.live.common.interfaces.enums.CommonStatusEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SkuInfoServiceImpl implements ISkuInfoService {
    
    @Resource
    private ISkuInfoMapper skuInfoMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SkuProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public List<SkuInfoPO> queryBySkuIds(List<Long> skuIdList) {
        LambdaQueryWrapper<SkuInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuInfoPO::getSkuId, skuIdList);
        queryWrapper.eq(SkuInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        return skuInfoMapper.selectList(queryWrapper);
    }

    @Override
    public SkuInfoPO queryBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuInfoPO::getSkuId, skuId);
        queryWrapper.eq(SkuInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return skuInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public SkuInfoPO queryBySkuIdFromCache(Long skuId) {
        String cacheKey = cacheKeyBuilder.buildSkuDetail(skuId);
        Object skuInfoCacheObj = redisTemplate.opsForValue().get(cacheKey);
        if (skuInfoCacheObj != null) {
            SkuInfoPO skuInfoPO = (SkuInfoPO) skuInfoCacheObj;
            //空值缓存
            if (skuInfoPO.getId() == null) {
                return null;
            }
            return skuInfoPO;
        }
        SkuInfoPO skuInfo = this.queryBySkuId(skuId);
        if (skuInfo != null) {
            redisTemplate.opsForValue().set(cacheKey, skuInfo, 1, TimeUnit.DAYS);
            return skuInfo;
        }
        //空值缓存
        redisTemplate.opsForValue().set(cacheKey, new SkuInfoPO(), 1, TimeUnit.DAYS);
        return null;
    }
}
