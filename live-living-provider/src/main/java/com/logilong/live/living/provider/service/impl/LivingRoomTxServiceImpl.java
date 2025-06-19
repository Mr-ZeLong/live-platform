package com.logilong.live.living.provider.service.impl;

import jakarta.annotation.Resource;
import com.logilong.live.framework.redis.starter.key.LivingProviderCacheKeyBuilder;
import com.logilong.live.common.interfaces.enums.CommonStatusEum;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.living.interfaces.dto.LivingRoomReqDTO;
import com.logilong.live.living.interfaces.dto.LivingRoomRespDTO;
import com.logilong.live.living.provider.dao.mapper.LivingRoomMapper;
import com.logilong.live.living.provider.dao.mapper.LivingRoomRecordMapper;
import com.logilong.live.living.provider.dao.po.LivingRoomRecordPO;
import com.logilong.live.living.provider.service.ILivingRoomService;
import com.logilong.live.living.provider.service.ILivingRoomTxService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class LivingRoomTxServiceImpl implements ILivingRoomTxService {

    @Resource
    private ILivingRoomService livingRoomService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private LivingRoomRecordMapper livingRoomRecordMapper;
    @Resource
    private LivingRoomMapper livingRoomMapper;
    @Resource
    private LivingProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomRespDTO livingRoomRespDTO = livingRoomService.queryByRoomId(livingRoomReqDTO.getRoomId());
        if (livingRoomRespDTO == null) {
            return false;
        }
        if (!(livingRoomRespDTO.getAnchorId().equals(livingRoomReqDTO.getAnchorId()))) {
            return false;
        }
        LivingRoomRecordPO livingRoomRecordPO = ConvertBeanUtils.convert(livingRoomRespDTO, LivingRoomRecordPO.class);
        livingRoomRecordPO.setEndTime(new Date());
        livingRoomRecordPO.setStatus(CommonStatusEum.INVALID_STATUS.getCode());
        livingRoomRecordMapper.insert(livingRoomRecordPO);
        livingRoomMapper.deleteById(livingRoomRecordPO.getId());
        //移除掉直播间cache
        String cacheKey = cacheKeyBuilder.buildLivingRoomObj(livingRoomReqDTO.getRoomId());
        redisTemplate.delete(cacheKey);
        return true;
    }
}
