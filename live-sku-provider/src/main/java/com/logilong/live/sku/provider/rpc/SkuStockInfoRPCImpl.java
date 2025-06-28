package com.logilong.live.sku.provider.rpc;

import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.framework.redis.starter.key.SkuProviderCacheKeyBuilder;
import com.logilong.live.sku.dto.SkuStockInfoDTO;
import com.logilong.live.sku.interfaces.ISkuStockInfoRPC;
import com.logilong.live.sku.provider.dao.po.SkuStockInfoPO;
import com.logilong.live.sku.provider.service.IAnchorShopInfoService;
import com.logilong.live.sku.provider.service.ISkuStockInfoService;
import com.logilong.live.sku.provider.service.bo.DecrStockNumBO;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@DubboService
public class SkuStockInfoRPCImpl implements ISkuStockInfoRPC {
    
    @Resource
    private ISkuStockInfoService skuStockInfoService;
    @Resource
    private IAnchorShopInfoService anchorShopInfoService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SkuProviderCacheKeyBuilder cacheKeyBuilder;
    private static final int MAX_RETRY_TIMES = 3;

    @Override
    public boolean updateStockNumBySkuId(Long skuId, Integer num) {
        for (int i = 0; i < MAX_RETRY_TIMES; i++) {
            DecrStockNumBO decrStockNumBO = skuStockInfoService.decrStockNumBySkuIdDB(skuId, num);
            if (decrStockNumBO.isEmptyStock()) {
                return false;
            }
            if (decrStockNumBO.isSuccess()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean decrStockNumBySkuIdCache(Long skuId, Integer num) {
        return skuStockInfoService.decrStockNumBySkuIdCache(skuId, num);
    }

    @Override
    public SkuStockInfoDTO queryBySkuId(Long skuId) {
        return ConvertBeanUtils.convert(skuStockInfoService.queryBySkuId(skuId), SkuStockInfoDTO.class);
    }

    @Override
    public boolean prepareStockInfo(Long anchorId) {
        List<Long> skuIdList = anchorShopInfoService.querySkuIdsByAnchorId(anchorId);
        List<SkuStockInfoPO> skuStockInfoPOS = skuStockInfoService.queryBySkuIds(skuIdList);
        //通常来说一个主播带货的个数不多，可能也就几十个商品，所以使用f0r循环也是可以的，如果带货商品数量大也可以用 redisTemplate.opsForValue().multiSet();
//        for (SkuStockInfo skuStockInfo : stockInfoList) {
//            Long skuId = skuStockInfo.getSkuId();
//            String cacheKey = keyBuilder.buildSkuStock(skuId);
//            redisTemplate.opsForValue().set(cacheKey, skuStockInfo.getStockNum(), 1, TimeUnit.DAYS);
//        }
        Map<String, Integer> cacheKeyMap = skuStockInfoPOS.stream()
                .collect(Collectors.toMap(skuStockInfoPO -> cacheKeyBuilder.buildSkuStock(skuStockInfoPO.getSkuId()), SkuStockInfoPO::getStockNum));
        redisTemplate.opsForValue().multiSet(cacheKeyMap);
        redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (String key : cacheKeyMap.keySet()) {
                    operations.expire((K) key, 1L, TimeUnit.DAYS);
                }
                return null;
            }
        });
        return true;
    }

    @Override
    public Integer queryStockNum(Long skuId) {
        String cacheKey = cacheKeyBuilder.buildSkuStock(skuId);
        Object stockObj = redisTemplate.opsForValue().get(cacheKey);
        return stockObj == null ? null : (Integer) stockObj;
    }

    @Override
    public boolean syncStockNumToMySql(Long anchorId) {
        List<Long> skuIdList = anchorShopInfoService.querySkuIdsByAnchorId(anchorId);
        for (Long skuId : skuIdList) {
            Integer stockNum = this.queryStockNum(skuId);
            if (stockNum != null) {
                skuStockInfoService.updateStockNumBySkuId(skuId, stockNum);
            }
        }
        return true;
    }
}
