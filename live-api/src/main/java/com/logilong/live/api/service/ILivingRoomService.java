package com.logilong.live.api.service;

import com.logilong.live.api.vo.LivingRoomInitVO;
import com.logilong.live.api.vo.req.LivingRoomReqVO;
import com.logilong.live.api.vo.req.OnlinePkReqVO;
import com.logilong.live.api.vo.resp.LivingRoomPageRespVO;

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
    boolean onlinePk(OnlinePkReqVO onlinePkReqVO);

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

}
