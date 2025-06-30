package com.logilong.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.gift.dto.RedPacketConfigReqDTO;
import com.logilong.live.gift.dto.RedPacketConfigRespDTO;
import com.logilong.live.gift.dto.RedPacketReceiveDTO;
import com.logilong.live.gift.interfaces.IRedPacketConfigRPC;
import com.logilong.live.gift.provider.dao.po.RedPacketConfigPO;
import com.logilong.live.gift.provider.service.IRedPacketConfigService;

@DubboService
public class RedPacketConfigRPCImpl implements IRedPacketConfigRPC {
    
    @Resource
    private IRedPacketConfigService redPacketConfigService;

    @Override
    public RedPacketConfigRespDTO queryByAnchorId(Long anchorId) {
        return ConvertBeanUtils.convert(redPacketConfigService.queryByAnchorId(anchorId), RedPacketConfigRespDTO.class);
    }

    @Override
    public boolean updateById(RedPacketConfigRespDTO redPacketConfigRespDTO) {
        return redPacketConfigService.updateById(ConvertBeanUtils.convert(redPacketConfigRespDTO, RedPacketConfigPO.class));
    }

    @Override
    public boolean addOne(RedPacketConfigReqDTO redPacketConfigReqDTO) {
        return redPacketConfigService.addOne(ConvertBeanUtils.convert(redPacketConfigReqDTO, RedPacketConfigPO.class));
    }

    @Override
    public boolean prepareRedPacket(Long anchorId) {
        return redPacketConfigService.prepareRedPacket(anchorId);
    }

    @Override
    public RedPacketReceiveDTO receiveRedPacket(RedPacketConfigReqDTO redPacketConfigReqDTO) {
        return redPacketConfigService.receiveRedPacket(redPacketConfigReqDTO);
    }

    @Override
    public Boolean startRedPacket(RedPacketConfigReqDTO reqDTO) {
        return redPacketConfigService.startRedPacket(reqDTO);
    }
}
