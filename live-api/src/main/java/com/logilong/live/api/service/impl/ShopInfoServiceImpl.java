package com.logilong.live.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import com.logilong.live.api.service.IShopInfoService;
import com.logilong.live.api.vo.req.PrepareOrderVO;
import com.logilong.live.api.vo.req.ShopCarReqVO;
import com.logilong.live.api.vo.req.SkuInfoReqVO;
import com.logilong.live.api.vo.resp.ShopCarRespVO;
import com.logilong.live.api.vo.resp.SkuDetailInfoVO;
import com.logilong.live.api.vo.resp.SkuInfoVO;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.gift.dto.*;
import com.logilong.live.gift.interfaces.IShopCarRpc;
import com.logilong.live.gift.interfaces.ISkuInfoRpc;
import com.logilong.live.gift.interfaces.ISkuOrderInfoRpc;
import com.logilong.live.gift.interfaces.ISkuStockInfoRpc;
import com.logilong.live.web.starter.context.LiveRequestContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopInfoServiceImpl implements IShopInfoService {
    
    @DubboReference
    private ISkuInfoRpc skuInfoRpc;
    @DubboReference
    private IShopCarRpc shopCarRpc;
    @DubboReference
    private ISkuOrderInfoRpc skuOrderInfoRpc;
    @DubboReference
    private ISkuStockInfoRpc skuStockInfoRpc;

    @Override
    public List<SkuInfoVO> queryByAnchorId(Long anchorId) {
        List<SkuInfoDTO> skuInfoDTOS = skuInfoRpc.queryByAnchorId(anchorId);
        return ConvertBeanUtils.convertList(skuInfoDTOS, SkuInfoVO.class);
    }

    @Override
    public SkuDetailInfoVO detail(SkuInfoReqVO skuInfoReqVO) {
        return ConvertBeanUtils.convert(skuInfoRpc.queryBySkuId(skuInfoReqVO.getSkuId(), skuInfoReqVO.getAnchorId()), SkuDetailInfoVO.class);
    }

    @Override
    public Boolean addCar(ShopCarReqVO reqVO) {
        return shopCarRpc.addCar(new ShopCarReqDTO(LiveRequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
    }

    @Override
    public Boolean removeFromCar(ShopCarReqVO reqVO) {
        return shopCarRpc.removeFromCar(new ShopCarReqDTO(LiveRequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
    }

    @Override
    public Boolean clearShopCar(ShopCarReqVO reqVO) {
        return shopCarRpc.clearShopCar(new ShopCarReqDTO(LiveRequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
    }

    @Override
    public Boolean addCarItemNum(ShopCarReqVO reqVO) {
        return shopCarRpc.addCarItemNum(new ShopCarReqDTO(LiveRequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
    }

    @Override
    public ShopCarRespVO getCarInfo(ShopCarReqVO reqVO) {
        ShopCarRespDTO carInfo = shopCarRpc.getCarInfo(new ShopCarReqDTO(LiveRequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
        ShopCarRespVO respVO = ConvertBeanUtils.convert(carInfo, ShopCarRespVO.class);
        respVO.setShopCarItemRespDTOS(carInfo.getSkuCarItemRespDTODTOS());
        return respVO;
    }

    @Override
    public SkuPrepareOrderInfoDTO prepareOrder(PrepareOrderVO prepareOrderVO) {
        PrepareOrderReqDTO reqDTO = new PrepareOrderReqDTO();
        reqDTO.setRoomId(prepareOrderVO.getRoomId());
        reqDTO.setUserId(LiveRequestContext.getUserId());
        return skuOrderInfoRpc.prepareOrder(reqDTO);
    }

    @Override
    public boolean prepareStock(Long anchorId) {
        return skuStockInfoRpc.prepareStockInfo(anchorId);
    }

    @Override
    public boolean payNow(PrepareOrderVO prepareOrderVO) {
        return skuOrderInfoRpc.payNow(LiveRequestContext.getUserId(), prepareOrderVO.getRoomId());
    }
}
