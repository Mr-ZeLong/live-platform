package com.logilong.live.sku.provider.service;



import com.logilong.live.sku.dto.RollbackStockInfoDTO;
import com.logilong.live.sku.provider.dao.po.SkuStockInfoPO;
import com.logilong.live.sku.provider.service.bo.DecrStockNumBO;

import java.util.List;

public interface ISkuStockInfoService {

    /**
     * 通过skuId查询库存信息
     */
    SkuStockInfoPO queryBySkuId(Long skuId);

    /**
     * 批量sku信息查询
     */
    List<SkuStockInfoPO> queryBySkuIds(List<Long> skuIdList);

    /**
     * 更新sku库存
     */
    boolean updateStockNumBySkuId(Long skuId, Integer stockNum);

    /**
     * 减sku库存 db
     */
    DecrStockNumBO decrStockNumBySkuIdDB(Long skuId, Integer num);

    /**
     * 扣件sku库存 redis
     */
    boolean decrStockNumBySkuIdsCache(List<Long> skuIdList, Integer num);

    /**
     * 扣件sku库存 redis
     */
    boolean decrStockNumBySkuIdCache(Long skuId, Integer num);

    /**
     * 库存回滚逻辑处理
     */
    boolean stockRollbackHandler(RollbackStockInfoDTO rollbackStockInfoDTO);

}
