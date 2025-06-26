package com.logilong.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class GiftProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String GIFT_CONFIG_CACHE = "gift_config_cache";
    private static final String GIFT_LIST_CACHE = "gift_list_cache";
    private static final String GIFT_CONSUME_KEY = "gift_consume_key";
    private static final String GIFT_LIST_LOCK = "gift_list_lock";
    private static final String LIVING_PK_KEY = "living_pk_key";
    private static final String LIVING_PK_SEND_SEQ = "living_pk_send_seq";
    private static final String LIVING_PK_IS_OVER = "living_pk_is over";
    private static final String RED_PACKET_LIST = "red_packet_list";
    private static final String RED_PACKET_INIT_LOCK = "red_packet_init_lock";
    private static final String RED_PACKET_TOTAL_GET_COUNT = "red_packet_total_get_count";
    private static final String RED_PACKET_TOTAL_GET_PRICE = "red_packet_total_get_price";
    private static final String RED_PACKET_MAX_GET_PRICE = "red_packet_max_get_price";
    private static final String USER_TOTAL_GET_PRICE_CACHE = "red_packet_user_total_get_price";
    private static final String RED_PACKET_PREPARE_SUCCESS = "red_packet_prepare_success";
    private static final String RED_PACKET_NOTIFY = "red_packet_notify";
    private static final String SKU_DETAIL_INFO_MAP = "sku_detail_info_map";
    private static final String SHOP_CAR = "shop_car";
    private static final String SKU_STOCK = "sku_stock";
    private static final String STOCK_SYNC_LOCK = "stock_sync_lock";
    private static final String SKU_DETAIL = "sku_detail";

    public String buildStockSyncLock() {
        return super.getPrefix() + STOCK_SYNC_LOCK;
    }


    public String buildSkuStock(Long skuId) {
        return super.getPrefix() + SKU_STOCK + super.getSplitItem() + skuId;
    }


    public String buildUserShopCar(Long userId, Integer roomId) {
        return super.getPrefix() + SHOP_CAR + super.getSplitItem() + userId + super.getSplitItem() + roomId;
    }

    public String buildSkuDetailInfoMap(Long anchorId) {
        return super.getPrefix() + SKU_DETAIL_INFO_MAP + super.getSplitItem() + anchorId;
    }

    public String buildSkuDetail(Long skuId) {
        return super.getPrefix() + SKU_DETAIL + super.getSplitItem() + skuId;
    }

    public String buildLivingPkIsOver(Integer roomId) {
        return super.getPrefix() + LIVING_PK_IS_OVER + super.getSplitItem() + roomId;
    }

    public String buildLivingPkSendSeq(Integer roomId) {
        return super.getPrefix() + LIVING_PK_SEND_SEQ + super.getSplitItem() + roomId;
    }

    public String buildLivingPkKey(Integer roomId) {
        return super.getPrefix() + LIVING_PK_KEY + super.getSplitItem() + roomId;
    }

    public String buildGiftConsumeKey(String uuid) {
        return super.getPrefix() + GIFT_CONSUME_KEY + super.getSplitItem() + uuid;
    }

    public String buildGiftConfigCacheKey(int giftId) {
        return super.getPrefix() + GIFT_CONFIG_CACHE + super.getSplitItem() + giftId;
    }

    public String buildGiftListCacheKey() {
        return super.getPrefix() + GIFT_LIST_CACHE;
    }

    public String buildGiftListLockCacheKey() {
        return super.getPrefix() + GIFT_LIST_LOCK;
    }

    public String buildRedPacketList(String code) {
        return super.getPrefix() + RED_PACKET_LIST + super.getSplitItem() + code;
    }

    public String buildRedPacketInitLock(String code) {
        return super.getPrefix() + RED_PACKET_INIT_LOCK + super.getSplitItem() + code;
    }

    public String buildRedPacketTotalGetCount(String code) {
        return super.getPrefix() + RED_PACKET_TOTAL_GET_COUNT + super.getSplitItem() + (Math.abs(code.hashCode()) % 100);
    }

    public String buildRedPacketTotalGetPrice(String code) {
        return super.getPrefix() + RED_PACKET_TOTAL_GET_PRICE + super.getSplitItem() + (Math.abs(code.hashCode()) % 100);
    }

    public String buildRedPacketMaxGetPrice(String code) {
        return super.getPrefix() + RED_PACKET_MAX_GET_PRICE + super.getSplitItem() + (Math.abs(code.hashCode()) % 100);
    }

    public String buildUserTotalGetPrice(Long userId) {
        return super.getPrefix() + USER_TOTAL_GET_PRICE_CACHE + super.getSplitItem() + userId;
    }

    public String buildRedPacketPrepareSuccess(String code) {
        return super.getPrefix() + RED_PACKET_PREPARE_SUCCESS + super.getSplitItem() + code;
    }

    public String buildRedPacketNotify(String code) {
        return super.getPrefix() + RED_PACKET_NOTIFY + super.getSplitItem() + code;
    }
}
