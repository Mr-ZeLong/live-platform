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
     * 开启直播间
     */
    Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 关闭直播间
     */
    boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 根据用户id查询是否正在开播
     */
    LivingRoomRespDTO queryByRoomId(Integer roomId);

    /**
     * 根据主播id查询直播间
     */
    LivingRoomRespDTO queryByAnchorId(Long anchorId);

    /**
     * 直播间列表的分页查询
     */
    PageWrapper<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 从DB查询对应类型所有的直播间列表
     */
    List<LivingRoomRespDTO> listAllLivingRoomFromDB(Integer type);

    /**
     * 用户登录在线roomId与userId关联处理
     */
    void userOnlineHandler(ImOnlineDTO imOnlineDTO);

    /**
     * 用户离线roomId与userId关联处理
     */
    void userOfflineHandler(ImOfflineDTO imOfflineDTO);

    /**
     * 支持根据roomId查询出批量的userId（set）存储，3000个人，元素非常多，O(n)
     */
    List<Long> queryUserIdsByRoomId(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 用户在pk直播间中，连上线请求
     */
    LivingPkRespDTO onlinePk(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 用户在pk直播间下线
     */
    boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 根据roomId查询当前pk人是谁
     */
    Long queryOnlinePkUserId(Integer roomId);
}
