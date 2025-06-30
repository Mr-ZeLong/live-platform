package com.logilong.live.sku.provider.service.impl;

import com.logilong.live.framework.redis.starter.key.SkuProviderCacheKeyBuilder;
import com.logilong.live.sku.dto.ShopCarItemRespDTO;
import com.logilong.live.sku.dto.ShopCarReqDTO;
import com.logilong.live.sku.dto.ShopCarRespDTO;
import com.logilong.live.sku.dto.SkuInfoDTO;
import com.logilong.live.sku.provider.dao.po.SkuInfoPO;
import com.logilong.live.sku.provider.service.IShopCarService;
import com.logilong.live.sku.provider.service.ISkuInfoService;
import jakarta.annotation.Resource;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopCarServiceImpl implements IShopCarService {
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SkuProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private ISkuInfoService skuInfoService;

    /**
     * 因为是以直播间为维度的购物车，所以不需要持久化，用缓存即可
     */
    @Override
    public Boolean addShopCar(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildShopCar(shopCarReqDTO.getUserId(), Long.valueOf(shopCarReqDTO.getRoomId()));
        redisTemplate.opsForHash().put(cacheKey, String.valueOf(shopCarReqDTO.getSkuId()), 1);
        return true;
    }

    @Override
    public Boolean removeFromShopCar(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildShopCar(shopCarReqDTO.getUserId(), Long.valueOf(shopCarReqDTO.getRoomId()));
        redisTemplate.opsForHash().delete(cacheKey, String.valueOf(shopCarReqDTO.getSkuId()));
        return true;
    }

    @Override
    public Boolean clearShopCar(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildShopCar(shopCarReqDTO.getUserId(), Long.valueOf(shopCarReqDTO.getRoomId()));
        redisTemplate.delete(cacheKey);
        return true;
    }

    @Override
    public Boolean addShopCarItemNum(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildShopCar(shopCarReqDTO.getUserId(), Long.valueOf(shopCarReqDTO.getRoomId()));
        redisTemplate.opsForHash().increment(cacheKey, String.valueOf(shopCarReqDTO.getSkuId()), 1);
        return true;
    }

    public ShopCarRespDTO getShopCarInfo(ShopCarReqDTO reqDTO) {
        String cacheKey = cacheKeyBuilder.buildShopCar(reqDTO.getUserId(), Long.valueOf(reqDTO.getRoomId()));
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(cacheKey, ScanOptions.scanOptions().match("*").build());
        Map<Long, Integer> shopCarItemCountMap = new HashMap<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            Long skuId = Long.valueOf((String) entry.getKey());
            Integer count = (Integer) entry.getValue();
            shopCarItemCountMap.put(skuId, count);
        }
        List<SkuInfoPO> skuInfoList = !shopCarItemCountMap.isEmpty() ? skuInfoService.queryBySkuIds(shopCarItemCountMap.keySet().stream().toList()) : new ArrayList<>();
        List<SkuInfoDTO> skuInfoDTOList = ConvertBeanUtils.convertList(skuInfoList, SkuInfoDTO.class);
        List<ShopCarItemRespDTO> shopCarItemRespDTOList = new ArrayList<>();
        Long totalPrice = 0L;
        for (SkuInfoDTO skuInfoDTO : skuInfoDTOList) {
            totalPrice += skuInfoDTO.getSkuPrice();
            shopCarItemRespDTOList.add(new ShopCarItemRespDTO(shopCarItemCountMap.get(skuInfoDTO.getSkuId()), skuInfoDTO));
        }
        ShopCarRespDTO shopCarRespDTO = new ShopCarRespDTO();
        shopCarRespDTO.setUserId(reqDTO.getUserId());
        shopCarRespDTO.setRoomId(Long.valueOf(reqDTO.getRoomId()));
        shopCarRespDTO.setTotalPrice(totalPrice);
        shopCarRespDTO.setShopCarItemRespDTOList(shopCarItemRespDTOList);
        return shopCarRespDTO;
    }

}
