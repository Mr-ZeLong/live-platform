package com.logilong.live.living.provider.service;

import com.logilong.live.common.interfaces.dto.PageWrapper;
import com.logilong.live.im.core.server.interfaces.dto.ImOfflineDTO;
import com.logilong.live.im.core.server.interfaces.dto.ImOnlineDTO;
import com.logilong.live.living.interfaces.dto.LivingPkRespDTO;
import com.logilong.live.living.interfaces.dto.LivingRoomReqDTO;
import com.logilong.live.living.interfaces.dto.LivingRoomRespDTO;

import java.util.List;


public interface ILivingRoomService {

    /**
     * 支持根据roomId查询出批量的userId（set）存储，3000个人，元素非常多，O(n)
     *
     * @param livingRoomReqDTO
     * @return
     */
    List<Long> queryUserIdByRoomId(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 用户下线处理
     *
     * @param imOfflineDTO
     */
    void userOfflineHandler(ImOfflineDTO imOfflineDTO);

    /**
     * 用户上线处理
     *
     * @param imOnlineDTO
     */
    void userOnlineHandler(ImOnlineDTO imOnlineDTO);

     /**
     * 查询所有的直播间类型
     *
     * @param type
     * @return
     */
    List<LivingRoomRespDTO> listAllLivingRoomFromDB(Integer type);

    /**
     * 直播间列表的分页查询
     *
     * @param livingRoomReqDTO
     * @return
     */
    PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 根据roomId查询直播间
     *
     * @param roomId
     * @return
     */
    LivingRoomRespDTO queryByRoomId(Integer roomId);

    /**
     * 开启直播间
     *
     * @param livingRoomReqDTO
     * @return
     */
    Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO);


    /**
     * 根据roomId查询当前pk人是谁
     *
     * @param roomId
     * @return
     */
    Long queryOnlinePkUserId(Integer roomId);

    /**
     * 用户在pk直播间中，连上线请求
     *
     * @param livingRoomReqDTO
     * @return
     */
    LivingPkRespDTO onlinePk(LivingRoomReqDTO livingRoomReqDTO);


    /**
     * 用户在pk直播间中，下线请求
     *
     * @param livingRoomReqDTO
     * @return
     */
    boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO);
}
