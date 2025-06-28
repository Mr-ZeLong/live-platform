package com.logilong.live.sku.interfaces;

import com.logilong.live.sku.dto.SkuStockInfoDTO;

public interface ISkuStockInfoRPC {

    /**
     * 通过skuId查询库存信息
     */
    SkuStockInfoDTO queryBySkuId(Long skuId);

    //库存值从mysql预热加载到redis中

    /**
     * 预热库存信息
     */
    boolean prepareStockInfo(Long anchorId);

    //提供基础的缓存查询接口

    /**
     * 基础的缓存查询接口
     */
    Integer queryStockNum(Long skuId);
    //设计一个接口用于同步redis值到mysql中（定时任务执行，本地定时任务去完成同步行为）

    /**
     * 同步库存信息到MySql中
     */
    boolean syncStockNumToMySql(Long anchorId);
    //库存扣减要设计Lua脚本

    /**
     * 更新sku库存
     */
    boolean updateStockNumBySkuId(Long skuId, Integer num);
    //库存扣减要设计Lua脚本

    /**
     * 扣件sku库存 Redis
     */
    boolean decrStockNumBySkuIdCache(Long skuId, Integer num);
    //库存扣减成功后，生成待支付订单(MQ)
}
