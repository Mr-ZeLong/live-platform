package com.logilong.live.sku.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.logilong.live.framework.redis.starter.key.SkuProviderCacheKeyBuilder;
import com.logilong.live.sku.constants.SkuOrderInfoEnum;
import com.logilong.live.sku.dto.RollbackStockInfoDTO;
import com.logilong.live.sku.dto.SkuOrderInfoReqDTO;
import com.logilong.live.sku.dto.SkuOrderInfoRespDTO;
import com.logilong.live.sku.provider.dao.mapper.ISkuStockInfoMapper;
import com.logilong.live.sku.provider.dao.po.SkuStockInfoPO;
import com.logilong.live.sku.provider.service.ISkuOrderInfoService;
import com.logilong.live.sku.provider.service.ISkuStockInfoService;
import com.logilong.live.sku.provider.service.bo.DecrStockNumBO;
import jakarta.annotation.Resource;
import com.logilong.live.common.interfaces.enums.CommonStatusEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SkuStockInfoServiceImpl implements ISkuStockInfoService {

    @Resource
    private ISkuStockInfoMapper skuStockInfoMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SkuProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private ISkuOrderInfoService skuOrderInfoService;

    private final String LUA_SCRIPT =
            "if (redis.call('exists',KEYS[1])) == 1 then " +
                    " local currentStock=redis.call('get',KEYS[1]) " +
                    "   if (tonumber(currentStock)>0 and tonumber(currentStock)-tonumber(ARGV[1])>=0) then " +
                    "       return redis.call('decrby',KEYS[1],tonumber(ARGV[1])) " +
                    "   else return -1 end " +
                    "else " +
                    "return -1 end";

    private final String BATCH_LUA_SCRIPT = "for i=1, ARGV[2] do    \n" +
            "   if (redis.call('exists', KEYS[i]))~= 1 then return -1 end\n" +
            "\tlocal currentStock=redis.call('get',KEYS[i])  \n" +
            "\tif (tonumber(currentStock)<=0 and tonumber(currentStock)-tonumber(ARGV[1])<0) then\n" +
            "       return -1\n" +
            "\tend\n" +
            "end  \n" +
            "\n" +
            "for  j=1,ARGV[2] do \n" +
            "\tredis.call('decrby',KEYS[j],tonumber(ARGV[1]))\n" +
            "end \n" +
            "return 1";

    @Override
    public SkuStockInfoPO queryBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuStockInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuStockInfoPO::getSkuId, skuId);
        queryWrapper.eq(SkuStockInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return skuStockInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SkuStockInfoPO> queryBySkuIds(List<Long> skuIdList) {
        LambdaQueryWrapper<SkuStockInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuStockInfoPO::getSkuId, skuIdList);
        queryWrapper.eq(SkuStockInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        return skuStockInfoMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateStockNumBySkuId(Long skuId, Integer stockNum) {
        LambdaUpdateWrapper<SkuStockInfoPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SkuStockInfoPO::getSkuId, skuId);
        SkuStockInfoPO skuStockInfoPO = new SkuStockInfoPO();
        skuStockInfoPO.setStockNum(stockNum);
        skuStockInfoPO.setSkuId(skuId);
        return skuStockInfoMapper.update(skuStockInfoPO, updateWrapper) > 0;
    }

    @Override
    public DecrStockNumBO decrStockNumBySkuIdDB(Long skuId, Integer num) {
        SkuStockInfoPO skuStockInfoPO = this.queryBySkuId(skuId);
        DecrStockNumBO decrStockNumBO = new DecrStockNumBO();
        if (skuStockInfoPO.getStockNum() == 0 || skuStockInfoPO.getStockNum() - num < 0) {
            decrStockNumBO.setEmptyStock(true);
            decrStockNumBO.setSuccess(false);
            return decrStockNumBO;
        }
        decrStockNumBO.setEmptyStock(false);
        boolean updateState = skuStockInfoMapper.decrStockNumBySkuId(skuId, num, skuStockInfoPO.getVersion()) > 0;
        decrStockNumBO.setSuccess(updateState);
        return decrStockNumBO;
    }

    @Override
    public boolean decrStockNumBySkuIdCache(Long skuId, Integer num) {
        //直接使用redis命令操作的话，可能会有多元请求 用Lua方案去替代进行改良
        //根据skuId查询库存信息，可能会有多元请求 网络请求
        //判断：skU库存值>0，sku库存值-num>0 (其他线程也在这么操)
        //扣减 decrby 网络请求 导致超卖
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_SCRIPT);
        script.setResultType(Long.class);
        String cacheKey = cacheKeyBuilder.buildSkuStock(skuId);
        return redisTemplate.execute(script, Collections.singletonList(cacheKey), num) >= 0;
    }

    @Override
    public boolean decrStockNumBySkuIdsCache(List<Long> skuIdList, Integer num) {
        //直接使用redis命令操作的话，可能会有多元请求 用Lua方案去替代进行改良
        //根据skuId查询库存信息，可能会有多元请求 网络请求
        //判断：skU库存值>0，sku库存值-num>0 (其他线程也在这么操)
        //扣减 decrby 网络请求 导致超卖
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(BATCH_LUA_SCRIPT);
        script.setResultType(Long.class);
        List<String> skuIdCacheKeyLIst = new ArrayList<>();
        for (Long skuId : skuIdList) {
            String cacheKey = cacheKeyBuilder.buildSkuStock(skuId);
            skuIdCacheKeyLIst.add(cacheKey);
        }
        return redisTemplate.execute(script, skuIdCacheKeyLIst, num, skuIdCacheKeyLIst.size()) >= 0;
    }

    @Override
    public boolean stockRollbackHandler(RollbackStockInfoDTO rollbackStockInfoDTO) {
        SkuOrderInfoRespDTO skuOrderInfoRespDTO = skuOrderInfoService.queryByOrderId(rollbackStockInfoDTO.getOrderId());
        if (skuOrderInfoRespDTO == null || skuOrderInfoRespDTO.getStatus() == SkuOrderInfoEnum.PAYED.getCode()) {
            return false;
        }
        SkuOrderInfoReqDTO skuOrderInfoReqDTO = new SkuOrderInfoReqDTO();
        skuOrderInfoReqDTO.setUserId(rollbackStockInfoDTO.getUserId());
        skuOrderInfoReqDTO.setStatus(SkuOrderInfoEnum.END.getCode());
        skuOrderInfoService.updateOrderStatus(skuOrderInfoReqDTO);
        //因为我们的直播带货场景比较特别，每件商品只能买一件
        List<Long> skuIdList = Arrays.stream(skuOrderInfoRespDTO.getSkuIdList().split(",")).toList().stream().map(Long::valueOf).toList();
        skuIdList.parallelStream().forEach(skuId -> {
            String cacheKey = cacheKeyBuilder.buildSkuStock(skuId);
            redisTemplate.opsForValue().increment(cacheKey, 1);
        });
        return true;
    }
}
