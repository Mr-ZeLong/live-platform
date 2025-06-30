package com.logilong.live.gift.interfaces;

import com.logilong.live.gift.dto.RedPacketConfigReqDTO;
import com.logilong.live.gift.dto.RedPacketConfigRespDTO;
import com.logilong.live.gift.dto.RedPacketReceiveDTO;

public interface IRedPacketConfigRPC {

    /**
     * 根据主播id查询有无发放红包雨的特权
     */
    RedPacketConfigRespDTO queryByAnchorId(Long anchorId);

    /**
     * 更新红包雨配置
     */
    boolean updateById(RedPacketConfigRespDTO redPacketConfigRespDTO);

    /**
     * 新增红包雨配置
     */
    boolean addOne(RedPacketConfigReqDTO redPacketConfigReqDTO);

    /**
     * 准备生成红包金额列表
     */
    boolean prepareRedPacket(Long anchorId);

    /**
     * 直播间用户领取红包
     */
    RedPacketReceiveDTO receiveRedPacket(RedPacketConfigReqDTO redPacketConfigReqDTO);

    /**
     * 开始红包雨
     */
    Boolean startRedPacket(RedPacketConfigReqDTO reqDTO);
    
}
