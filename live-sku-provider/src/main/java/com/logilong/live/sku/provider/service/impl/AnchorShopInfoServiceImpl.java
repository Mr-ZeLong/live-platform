package com.logilong.live.sku.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.logilong.live.sku.provider.dao.mapper.IAnchorShopInfoMapper;
import com.logilong.live.sku.provider.dao.po.AnchorShopInfoPO;
import com.logilong.live.sku.provider.service.IAnchorShopInfoService;
import jakarta.annotation.Resource;
import com.logilong.live.common.interfaces.enums.CommonStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnchorShopInfoServiceImpl implements IAnchorShopInfoService {
    
    @Resource
    private IAnchorShopInfoMapper anchorShopInfoMapper;
    
    @Override
    public List<Long> querySkuIdsByAnchorId(Long anchorId) {
        LambdaQueryWrapper<AnchorShopInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnchorShopInfoPO::getAnchorId, anchorId);
        queryWrapper.eq(AnchorShopInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.select(AnchorShopInfoPO::getSkuId);
        return anchorShopInfoMapper.selectList(queryWrapper).stream().map(AnchorShopInfoPO::getSkuId).collect(Collectors.toList());
    }

    @Override
    public List<Long> queryAllValidAnchorIds() {
        LambdaQueryWrapper<AnchorShopInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnchorShopInfoPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        return anchorShopInfoMapper.selectList(queryWrapper).stream().map(AnchorShopInfoPO::getAnchorId).collect(Collectors.toList());
    }
}
