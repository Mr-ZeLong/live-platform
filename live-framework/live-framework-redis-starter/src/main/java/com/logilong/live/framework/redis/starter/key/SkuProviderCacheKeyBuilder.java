package com.logilong.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


/**
 * 直播带货模块的redis缓存key构建
 **/
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class SkuProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String SKU_DETAIL = "sku_detail";
    private static final String SHOP_Car = "shop_Car";
    private static final String SKU_STOCK = "sku_stock";
    private static final String SKU_STOCK_SYNC_LOCK = "sku_stock_sync_lock";
    private static final String SKU_ORDER_INFO = "sku_order_info";
    private static final String SKU_ORDER_INFO_BY_ORDER_ID = "sku_order_info_by_order_id";

    public String buildSkuOrderInfoByOrderId(Long orderId) {
        return super.getPrefix() + SKU_ORDER_INFO_BY_ORDER_ID + super.getSplitItem() + orderId;
    }
    public String buildSkuOrderInfo(Long userId, Long roomId) {
        return super.getPrefix() + SKU_ORDER_INFO + super.getSplitItem() + userId + super.getSplitItem() + roomId;
    }
    public String buildSkuStockSyncLock() {
        return super.getPrefix() + SKU_STOCK_SYNC_LOCK;
    }
    public String buildSkuStock(Long skuId) {
        return super.getPrefix() + SKU_STOCK + super.getSplitItem() + skuId;
    }

    public String buildShopCar(Long userId, Long roomId) {
        return super.getPrefix() + SHOP_Car + super.getSplitItem() + userId + super.getSplitItem() + roomId;
    }

    public String buildSkuDetail(Long skuId) {
        return super.getPrefix() + SKU_DETAIL + super.getSplitItem() + skuId;
    }
}
