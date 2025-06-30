package com.logilong.live.api.service.impl;

import com.logilong.live.api.error.ApiErrorEnum;
import com.logilong.live.api.vo.PrepareOrderVO;
import com.logilong.live.api.vo.resp.ShopCarItemRespVO;
import com.logilong.live.living.interfaces.dto.LivingRoomRespDTO;
import com.logilong.live.living.interfaces.rpc.ILivingRoomRPC;
import com.logilong.live.sku.dto.*;
import com.logilong.live.sku.interfaces.IShopCarRPC;
import com.logilong.live.sku.interfaces.ISkuInfoRPC;
import com.logilong.live.sku.interfaces.ISkuOrderInfoRPC;
import com.logilong.live.sku.interfaces.ISkuStockInfoRPC;
import com.logilong.live.web.starter.error.BizBaseErrorEnum;
import com.logilong.live.web.starter.error.ErrorAssert;
import org.apache.dubbo.config.annotation.DubboReference;
import com.logilong.live.api.service.IShopInfoService;
import com.logilong.live.api.vo.req.ShopCarReqVO;
import com.logilong.live.api.vo.req.SkuInfoReqVO;
import com.logilong.live.api.vo.resp.ShopCarRespVO;
import com.logilong.live.api.vo.resp.SkuDetailInfoVO;
import com.logilong.live.api.vo.resp.SkuInfoVO;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.web.starter.context.LiveRequestContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ShopInfoServiceImpl implements IShopInfoService {
    
    @DubboReference
    private ISkuInfoRPC skuInfoRPC;
    @DubboReference
    private IShopCarRPC shopCarRPC;
    @DubboReference
    private ISkuOrderInfoRPC skuOrderInfoRPC;
    @DubboReference
    private ISkuStockInfoRPC skuStockInfoRPC;
    @DubboReference
    private ILivingRoomRPC livingRoomRPC;

    @Override
    public List<SkuInfoVO> queryByRoomId(Integer roomId) {
        LivingRoomRespDTO livingRoomRespDTO = livingRoomRPC.queryByRoomId(roomId);
        ErrorAssert.isNotNull(livingRoomRespDTO, BizBaseErrorEnum.PARAM_ERROR);
        List<SkuInfoDTO> skuInfoDTOList = skuInfoRPC.queryByAnchorId(livingRoomRespDTO.getAnchorId());
        ErrorAssert.isTure(!CollectionUtils.isEmpty(skuInfoDTOList), BizBaseErrorEnum.PARAM_ERROR);
        return ConvertBeanUtils.convertList(skuInfoDTOList, SkuInfoVO.class);
    }

    @Override
    public SkuDetailInfoVO detail(SkuInfoReqVO skuInfoReqVO) {
        return ConvertBeanUtils.convert(skuInfoRPC.queryBySkuId(skuInfoReqVO.getSkuId()), SkuDetailInfoVO.class);
    }

    @Override
    public Boolean addShopCar(ShopCarReqVO reqVO) {
        ShopCarReqDTO reqDTO = ConvertBeanUtils.convert(reqVO, ShopCarReqDTO.class);
        reqDTO.setUserId(LiveRequestContext.getUserId());
        return shopCarRPC.addShopCar(reqDTO);
    }

    @Override
    public Boolean removeFromShopCar(ShopCarReqVO reqVO) {
        ShopCarReqDTO reqDTO = ConvertBeanUtils.convert(reqVO, ShopCarReqDTO.class);
        reqDTO.setUserId(LiveRequestContext.getUserId());
        return shopCarRPC.removeFromShopCar(reqDTO);
    }

    @Override
    public Boolean clearShopCar(ShopCarReqVO reqVO) {
        ShopCarReqDTO reqDTO = ConvertBeanUtils.convert(reqVO, ShopCarReqDTO.class);
        reqDTO.setUserId(LiveRequestContext.getUserId());
        return shopCarRPC.clearShopCar(reqDTO);
    }

    @Override
    public Boolean addShopCarItemNum(ShopCarReqVO reqVO) {
        ShopCarReqDTO reqDTO = ConvertBeanUtils.convert(reqVO, ShopCarReqDTO.class);
        reqDTO.setUserId(LiveRequestContext.getUserId());
        return shopCarRPC.addShopCarItemNum(reqDTO);
    }

    @Override
    public ShopCarRespVO getShopCarInfo(ShopCarReqVO reqVO) {
        ShopCarReqDTO reqDTO = ConvertBeanUtils.convert(reqVO, ShopCarReqDTO.class);
        reqDTO.setUserId(LiveRequestContext.getUserId());
        ShopCarRespDTO shopCarRespDTO = shopCarRPC.getShopCarInfo(reqDTO);
        ShopCarRespVO shopCarRespVO = ConvertBeanUtils.convert(shopCarRespDTO, ShopCarRespVO.class);
        shopCarRespVO.setShopCarItemRespVOList(ConvertBeanUtils.convertList(shopCarRespDTO.getShopCarItemRespDTOList(), ShopCarItemRespVO.class));
        return shopCarRespVO;
    }

    @Override
    public SkuPrepareOrderInfoDTO prepareOrder(PrepareOrderVO prepareOrderVO) {
        PrepareOrderReqDTO reqDTO = new PrepareOrderReqDTO();
        reqDTO.setRoomId(prepareOrderVO.getRoomId());
        reqDTO.setUserId(LiveRequestContext.getUserId());
        return skuOrderInfoRPC.prepareOrder(reqDTO);
    }

    @Override
    public boolean prepareStock(Long anchorId) {
        return skuStockInfoRPC.prepareStockInfo(anchorId);
    }

    @Override
    public boolean payNow(PrepareOrderVO prepareOrderVO) {
        prepareOrderVO.setUserId(LiveRequestContext.getUserId());
        boolean isSuccess = skuOrderInfoRPC.payNow(ConvertBeanUtils.convert(prepareOrderVO, PayNowReqDTO.class));
        ErrorAssert.isTure(isSuccess, ApiErrorEnum.PAY_ERROR);
        return isSuccess;
    }
}
