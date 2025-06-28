package com.logilong.live.sku.provider.rpc;

import com.logilong.live.sku.dto.SkuDetailInfoDTO;
import com.logilong.live.sku.dto.SkuInfoDTO;
import com.logilong.live.sku.interfaces.ISkuInfoRPC;
import com.logilong.live.sku.provider.service.IAnchorShopInfoService;
import com.logilong.live.sku.provider.service.ISkuInfoService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;

import java.util.List;


@DubboService
public class SkuInfoRPCImpl implements ISkuInfoRPC {

    @Resource
    private ISkuInfoService skuInfoService;
    @Resource
    private IAnchorShopInfoService anchorShopInfoService;

    @Override
    public List<SkuInfoDTO> queryByAnchorId(Long anchorId) {
        List<Long> idsList = anchorShopInfoService.querySkuIdsByAnchorId(anchorId);
        return ConvertBeanUtils.convertList(skuInfoService.queryBySkuIds(idsList), SkuInfoDTO.class);
    }

    @Override
    public SkuDetailInfoDTO queryBySkuId(Long skuId) {
        return ConvertBeanUtils.convert(skuInfoService.queryBySkuIdFromCache(skuId), SkuDetailInfoDTO.class);
    }
}
