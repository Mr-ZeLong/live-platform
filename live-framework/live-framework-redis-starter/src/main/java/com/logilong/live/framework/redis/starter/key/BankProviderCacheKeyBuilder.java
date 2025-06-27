package com.logilong.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 支付中台模块的RedisKey构建
 */

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class BankProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static final String BALANCE_CACHE = "balance_cache";
    private static final String PAY_PRODUCT_CACHE = "pay_product_cache";
    private static final String PAY_PRODUCT_ITEM_CACHE = "pay_product_item_cache";

    public String buildPayProductItemCache(Long productId) {
        return super.getPrefix() + PAY_PRODUCT_ITEM_CACHE + super.getSplitItem() + productId;
    }

    /**
     * 按照产品的类型来进行检索
     */
    public String buildPayProductCache(Integer type) {
        return super.getPrefix() + PAY_PRODUCT_CACHE + super.getSplitItem() + type;
    }

    /**
     * 构建用户余额cache key
     */
    public String buildUserBalance(Long userId) {
        return super.getPrefix() + BALANCE_CACHE + super.getSplitItem() + userId;
    }

}
