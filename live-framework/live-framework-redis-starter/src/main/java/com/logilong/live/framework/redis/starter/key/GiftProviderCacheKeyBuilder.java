package com.logilong.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 礼物红包模块的RedisKey构建
 */

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
    private static final String RED_PACKET_TOTAL_GET_CACHE = "red_packet_total_get_cache";
    private static final String RED_PACKET_TOTAL_GET_PRICE_CACHE = "red_packet_total_get_price_cache";
    private static final String MAX_GET_PRICE_CACHE = "max_get_price_cache";
    private static final String USER_TOTAL_GET_PRICE_CACHE = "user_total_get_price_cache";
    private static final String RED_PACKET_PREPARE_SUCCESS = "red_packet_prepare_success";
    private static final String RED_PACKET_NOTIFY = "red_packet_notify";

    public String buildRedPacketNotifyCache(String code) {
        return super.getPrefix() + RED_PACKET_NOTIFY + super.getSplitItem() + code;
    }
    public String buildRedPacketPrepareSuccessCache(String code) {
        return super.getPrefix() + RED_PACKET_PREPARE_SUCCESS + super.getSplitItem() + code;
    }

    public String buildUserTotalGetPriceCache(Long userId) {
        return super.getPrefix() + USER_TOTAL_GET_PRICE_CACHE + super.getSplitItem() + userId;
    }

    public String buildMaxGetPriceCache(String code) {
        return super.getPrefix() + MAX_GET_PRICE_CACHE + super.getSplitItem() + code;
    }

    public String buildRedPacketTotalGetPriceCache(String code) {
        return super.getPrefix() + RED_PACKET_TOTAL_GET_PRICE_CACHE + super.getSplitItem() + code;
    }

    public String buildRedPacketTotalGetCache(String code) {
        return super.getPrefix() + RED_PACKET_TOTAL_GET_CACHE + super.getSplitItem() + code;
    }

    public String buildRedPacketInitLock(String code) {
        return super.getPrefix() + RED_PACKET_INIT_LOCK + super.getSplitItem() + code;
    }


    public String buildRedPacketList(String code) {
        return super.getPrefix() + RED_PACKET_LIST + super.getSplitItem() + code;
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
}
