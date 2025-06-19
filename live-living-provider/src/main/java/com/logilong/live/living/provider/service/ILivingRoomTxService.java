package com.logilong.live.living.provider.service;

import com.logilong.live.living.interfaces.dto.LivingRoomReqDTO;


public interface ILivingRoomTxService {

    /**
     * 关闭直播间
     *
     * @param livingRoomReqDTO
     * @return
     */
    boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO);

}
