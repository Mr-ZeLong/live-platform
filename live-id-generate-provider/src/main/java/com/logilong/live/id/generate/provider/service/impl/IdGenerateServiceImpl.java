package com.logilong.live.id.generate.provider.service.impl;

import com.logilong.live.id.generate.provider.dao.mapper.IdGenerateMapper;
import com.logilong.live.id.generate.provider.dao.po.IdGeneratePO;
import com.logilong.live.id.generate.provider.service.IdGenerateService;
import com.logilong.live.id.generate.provider.service.bo.LocalSeqIdBO;
import jakarta.annotation.Resource;
import com.logilong.live.id.generate.provider.service.bo.LocalUnSeqIdBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class IdGenerateServiceImpl implements IdGenerateService, InitializingBean {

    @Resource
    private IdGenerateMapper idGenerateMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerateServiceImpl.class);
    private static final Map<Integer, LocalSeqIdBO> localSeqIdBOMap = new ConcurrentHashMap<>();
    private static final Map<Integer, LocalUnSeqIdBO> localUnSeqIdBOMap = new ConcurrentHashMap<>();
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 16, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("id-generate-thread-" + ThreadLocalRandom.current().nextInt(1000));
                return thread;
            });
    private static final float UPDATE_RATE = 0.75f;
    private static final int SEQ_ID = 1;
    private static final Map<Integer, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    @Override
    public Long getUnSeqId(Integer id) {
        if (id == null) {
            LOGGER.error("[getSeqId] id is error,id is {}", (Object) null);
            return null;
        }
        LocalUnSeqIdBO localUnSeqIdBO = localUnSeqIdBOMap.get(id);
        if (localUnSeqIdBO == null) {
            LOGGER.error("[getUnSeqId] localUnSeqIdBO is null,id is {}", id);
            return null;
        }
        Long returnId = localUnSeqIdBO.getIdQueue().poll();
        if (returnId == null) {
            LOGGER.error("[getUnSeqId] returnId is null,id is {}", id);
            return null;
        }
        this.refreshLocalUnSeqId(localUnSeqIdBO);
        return returnId;
    }

    @Override
    public Long getSeqId(Integer id) {
        if (id == null) {
            LOGGER.error("[getSeqId] id is error,id is {}", id);
            return null;
        }
        LocalSeqIdBO localSeqIdBO = localSeqIdBOMap.get(id);
        if (localSeqIdBO == null) {
            LOGGER.error("[getSeqId] localSeqIdBO is null,id is {}", id);
            return null;
        }
        this.refreshLocalSeqId(localSeqIdBO);
        long returnId = localSeqIdBO.getCurrentNum().incrementAndGet();
        if (returnId > localSeqIdBO.getNextThreshold()) {
            //同步去刷新
            LOGGER.error("[getSeqId] id is over limit,id is {}", id);
            return null;
        }
        return returnId;
    }

    /**
     * 刷新本地有序id段
     */
    private void refreshLocalSeqId(LocalSeqIdBO localSeqIdBO) {
        long step = localSeqIdBO.getNextThreshold() - localSeqIdBO.getCurrentStart();
        if (localSeqIdBO.getCurrentNum().get() - localSeqIdBO.getCurrentStart() > step * UPDATE_RATE) {
            Semaphore semaphore = semaphoreMap.get(localSeqIdBO.getId());
            if (semaphore == null) {
                LOGGER.warn("[refreshLocalSeqId] semaphore is null, id: {}", localSeqIdBO.getId());
                return;
            }
            // 尝试获取信号量（非阻塞，直接返回获取结果）
            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                LOGGER.info("[refreshLocalSeqId] start to refresh local seq id segment, id: {}", localSeqIdBO.getId());
                // 异步进行id段刷新操作
                threadPoolExecutor.execute(() -> {
                    try {
                        // 双重检查：在获取信号量后再次检查是否仍需要刷新
                        // 如果获取到的本地无序id段对象与当前对象不一致，则说明该id段已被刷新，现在该id存放的id段对象是刷新后的对象，不需要再次刷新
                        if(localSeqIdBOMap.get(localSeqIdBO.getId()) != localSeqIdBO){
                            LOGGER.debug("[refreshLocalUnSeqId] seq id segment has been refreshed, no need to refresh it again after acquiring semaphore, id: {}", localSeqIdBO.getId());
                            return;
                        }
                        IdGeneratePO idGeneratePO = idGenerateMapper.selectById(localSeqIdBO.getId());
                        tryUpdateMySQLRecord(idGeneratePO);
                        LOGGER.info("[refreshLocalSeqId] successfully refreshed local seq id segment, id: {}", localSeqIdBO.getId());
                    } catch (Exception e) {
                        LOGGER.error("[refreshLocalSeqId] error occurred while refreshing local seq id segment, id: {}", localSeqIdBO.getId(), e);
                    } finally {
                        semaphoreMap.get(localSeqIdBO.getId()).release();
                    }
                });
            } else {
                LOGGER.debug("[refreshLocalSeqId] failed to acquire semaphore, another thread is refreshing id segment, id: {}", localSeqIdBO.getId());
            }
        }
    }

    /**
     * 刷新本地无序id段
     */
    private void refreshLocalUnSeqId(LocalUnSeqIdBO localUnSeqIdBO) {
        long begin = localUnSeqIdBO.getCurrentStart();
        long end = localUnSeqIdBO.getNextThreshold();
        long remainSize = localUnSeqIdBO.getIdQueue().size();
        //如果使用剩余空间不足25%，则进行刷新
        if ((end - begin) * 0.25 > remainSize) {
            Semaphore semaphore = semaphoreMap.get(localUnSeqIdBO.getId());
            if (semaphore == null) {
                LOGGER.warn("[refreshLocalUnSeqId] semaphore is null, id: {}", localUnSeqIdBO.getId());
                return;
            }
            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                LOGGER.info("[refreshLocalUnSeqId] start to refresh local unseq id segment, id: {}", localUnSeqIdBO.getId());
                threadPoolExecutor.execute(() -> {
                    try {
                        // 双重检查：在获取信号量后再次检查是否仍需要刷新
                        // 如果获取到的本地无序id段对象与当前对象不一致，则说明该id段已被刷新，现在该id存放的id段对象是刷新后的对象，不需要再次刷新
                        if(localUnSeqIdBOMap.get(localUnSeqIdBO.getId()) != localUnSeqIdBO) {
                            LOGGER.debug("[refreshLocalUnSeqId] unseq id segment has been refreshed, no need to refresh it again after acquiring semaphore, id: {}", localUnSeqIdBO.getId());
                            return;
                        }
                        // 正常刷新
                        IdGeneratePO idGeneratePO = idGenerateMapper.selectById(localUnSeqIdBO.getId());
                        tryUpdateMySQLRecord(idGeneratePO);
                        LOGGER.info("[refreshLocalUnSeqId] successfully refreshed local unseq id segment, id: {}", localUnSeqIdBO.getId());
                    } catch (Exception e) {
                        LOGGER.error("[refreshLocalUnSeqId] error occurred while refreshing local unseq id segment, id: {}", localUnSeqIdBO.getId(), e);
                    } finally {
                        // 释放信号量
                        semaphoreMap.get(localUnSeqIdBO.getId()).release();
                    }
                });
            } else {
                LOGGER.debug("[refreshLocalUnSeqId] failed to acquire semaphore, another thread is refreshing id segment, id: {}", localUnSeqIdBO.getId());
            }
        }
    }

    //bean初始化的时候会回调到这里
    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdGeneratePO> idGeneratePOList = idGenerateMapper.selectAll();
        for (IdGeneratePO idGeneratePO : idGeneratePOList) {
            LOGGER.info("服务刚启动，抢占新的id段");
            tryUpdateMySQLRecord(idGeneratePO);
            semaphoreMap.put(idGeneratePO.getId(), new Semaphore(1));
        }
    }

    /**
     * 更新mysql里面的分布式id的配置信息，占用相应的id段
     * 同步执行，很多的网络IO，性能较慢
     */
    private void tryUpdateMySQLRecord(IdGeneratePO idGeneratePO) {
        int updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
        if (updateResult > 0) {
            localIdBOHandler(idGeneratePO);
            return;
        }
        //重试进行更新，最多重试3次
        for (int i = 0; i < 3; i++) {
            idGeneratePO = idGenerateMapper.selectById(idGeneratePO.getId());
            updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
            if (updateResult > 0) {
                localIdBOHandler(idGeneratePO);
                return;
            }
        }
        throw new RuntimeException("表id段占用失败，竞争过于激烈，id is " + idGeneratePO.getId());
    }

    /**
     * 专门处理如何将本地ID对象放入到Map中，并且进行初始化的
     */
    private void localIdBOHandler(IdGeneratePO idGeneratePO) {
        long currentStart = idGeneratePO.getCurrentStart();
        long nextThreshold = idGeneratePO.getNextThreshold();
        if (idGeneratePO.getIsSeq() == SEQ_ID) {
            LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
            AtomicLong atomicLong = new AtomicLong(currentStart);
            localSeqIdBO.setId(idGeneratePO.getId());
            localSeqIdBO.setCurrentNum(atomicLong);
            localSeqIdBO.setCurrentStart(currentStart);
            localSeqIdBO.setNextThreshold(nextThreshold);
            localSeqIdBOMap.put(localSeqIdBO.getId(), localSeqIdBO);
        } else {
            LocalUnSeqIdBO localUnSeqIdBO = new LocalUnSeqIdBO();
            localUnSeqIdBO.setCurrentStart(currentStart);
            localUnSeqIdBO.setNextThreshold(nextThreshold);
            localUnSeqIdBO.setId(idGeneratePO.getId());
            long begin = localUnSeqIdBO.getCurrentStart();
            long end = localUnSeqIdBO.getNextThreshold();
            List<Long> idList = new ArrayList<>();
            for (long i = begin; i < end; i++) {
                idList.add(i);
            }
            //将本地id段提前打乱，然后放入到队列中
            Collections.shuffle(idList);
            ConcurrentLinkedQueue<Long> idQueue = new ConcurrentLinkedQueue<>(idList);
            localUnSeqIdBO.setIdQueue(idQueue);
            localUnSeqIdBOMap.put(localUnSeqIdBO.getId(), localUnSeqIdBO);
        }
    }
}
