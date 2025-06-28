package com.logilong.live.sku.provider.config;

import com.logilong.live.sku.interfaces.ISkuStockInfoRPC;
import jakarta.annotation.Resource;
import com.logilong.live.framework.redis.starter.key.SkuProviderCacheKeyBuilder;
import com.logilong.live.sku.provider.service.IAnchorShopInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 启动服务之后，每隔一段时间去同步直播间redis库存信息到DB中
 */
@Configuration
public class RefreshSkuStockNumJob implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshSkuStockNumJob.class);

    @Resource
    private ISkuStockInfoRPC skuStockInfoRPC;
    @Resource
    private IAnchorShopInfoService anchorShopInfoService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SkuProviderCacheKeyBuilder cacheKeyBuilder;

    private final ScheduledThreadPoolExecutor schedulePool = new ScheduledThreadPoolExecutor(1);

    @Override
    public void afterPropertiesSet() {
        //一秒钟刷新一次直播间列表数据
        schedulePool.scheduleWithFixedDelay(new RefreshCacheListJob(), 3000, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 在使用定时任务的时候需要注意，如果部署的时候，采用分布式节点进行部署，有可能多个节点同时执行定时任务，
     * 也就意味着下面的定时任务，将会有n多个进程同时执行，所以需要加分布式锁，确保只有加锁成功的任务，才能
     * 执行 refreshRedisToDB 方法
     */
    class RefreshCacheListJob implements Runnable {

        @Override
        public void run() {
            refreshRedisToDB();
        }
    }

    private void refreshRedisToDB() {
        String cacheKey = cacheKeyBuilder.buildSkuStockSyncLock();
        boolean lockStatus = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(cacheKey, 1, 10, TimeUnit.SECONDS));
        if (lockStatus) {
            List<Long> anchorIds = anchorShopInfoService.queryAllValidAnchorIds();
            for (Long anchorId : anchorIds) {
                skuStockInfoRPC.syncStockNumToMySql(anchorId);
            }
        }
    }
}
