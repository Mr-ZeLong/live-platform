package com.logilong.live.api.service;

import com.logilong.live.api.vo.LivingRoomInitVO;
import com.logilong.live.api.vo.req.LivingRoomReqVO;
import com.logilong.live.api.vo.req.OnlinePKReqVO;
import com.logilong.live.api.vo.resp.LivingRoomPageRespVO;
import com.logilong.live.api.vo.resp.RedPacketReceiveVO;

public interface ILivingRoomService {

    /**
     * 直播间列表展示
     *
     * @param livingRoomReqVO
     * @return
     */
    LivingRoomPageRespVO list(LivingRoomReqVO livingRoomReqVO);

    /**
     * 开启直播间
     *
     * @param type
     */
    Integer startingLiving(Integer type);


    /**
     * 用户在pk直播间中，连上线请求
     *
     * @param onlinePkReqVO
     * @return
     */
    boolean onlinePk(OnlinePKReqVO onlinePkReqVO);

    /**
     * 关闭直播间
     *
     * @param roomId
     * @return
     */
    boolean closeLiving(Integer roomId);

    /**
     * 根据用户id返回当前直播间相关信息
     *
     * @param userId
     * @param roomId
     * @return
     */
    LivingRoomInitVO anchorConfig(Long userId,Integer roomId);

    /**
     * 主播点击开始准备红包雨金额
     */
    Boolean prepareRedPacket(Long userId, Integer roomId);

    /**
     * 主播开始红包雨
     */
    Boolean startRedPacket(Long userId, String code);

    /**
     * 根据红包雨code领取红包
     */
    RedPacketReceiveVO receiveRedPacket(Long userId, String redPacketConfigCode);
}
