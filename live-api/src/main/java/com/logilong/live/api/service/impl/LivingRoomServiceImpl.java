package com.logilong.live.api.service.impl;

import com.logilong.live.api.vo.resp.RedPacketReceiveVO;
import com.logilong.live.gift.dto.RedPacketConfigReqDTO;
import com.logilong.live.gift.dto.RedPacketConfigRespDTO;
import com.logilong.live.gift.dto.RedPacketReceiveDTO;
import com.logilong.live.gift.interfaces.IRedPacketConfigRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import com.logilong.live.api.error.ApiErrorEnum;
import com.logilong.live.api.service.ILivingRoomService;

import com.logilong.live.api.vo.LivingRoomInitVO;
import com.logilong.live.api.vo.req.LivingRoomReqVO;
import com.logilong.live.api.vo.req.OnlinePKReqVO;
import com.logilong.live.api.vo.resp.LivingRoomPageRespVO;
import com.logilong.live.api.vo.resp.LivingRoomRespVO;
import com.logilong.live.common.interfaces.dto.PageWrapper;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.im.constants.AppIdEnum;
import com.logilong.live.living.interfaces.dto.LivingPkRespDTO;
import com.logilong.live.living.interfaces.dto.LivingRoomReqDTO;
import com.logilong.live.living.interfaces.dto.LivingRoomRespDTO;
import com.logilong.live.living.interfaces.rpc.ILivingRoomRPC;
import com.logilong.live.user.dto.UserDTO;
import com.logilong.live.user.interfaces.IUserRPC;
import com.logilong.live.web.starter.context.LiveRequestContext;
import com.logilong.live.web.starter.error.BizBaseErrorEnum;
import com.logilong.live.web.starter.error.ErrorAssert;
import com.logilong.live.web.starter.error.LiveErrorException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class LivingRoomServiceImpl implements ILivingRoomService {

    @DubboReference
    private IUserRPC userRpc;
    @DubboReference
    private ILivingRoomRPC livingRoomRpc;
    @DubboReference
    private IRedPacketConfigRpc redPacketConfigRpc;

    @Override
    public LivingRoomPageRespVO list(LivingRoomReqVO livingRoomReqVO) {
        PageWrapper<LivingRoomRespDTO>  resultPage = livingRoomRpc.list(ConvertBeanUtils.convert(livingRoomReqVO,LivingRoomReqDTO.class));
        LivingRoomPageRespVO livingRoomPageRespVO = new LivingRoomPageRespVO();
        livingRoomPageRespVO.setList(ConvertBeanUtils.convertList(resultPage.getList(), LivingRoomRespVO.class));
        livingRoomPageRespVO.setHasNext(resultPage.isHasNext());
        return livingRoomPageRespVO;
    }

    @Override
    public Integer startingLiving(Integer type) {
        Long userId = LiveRequestContext.getUserId();
        UserDTO userDTO = userRpc.getByUserId(userId);
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setAnchorId(userId);
        livingRoomReqDTO.setRoomName("主播-" + LiveRequestContext.getUserId() + "的直播间");
        livingRoomReqDTO.setCovertImg(userDTO.getAvatar());
        livingRoomReqDTO.setType(type);
        return livingRoomRpc.startLivingRoom(livingRoomReqDTO);
    }

    @Override
    public boolean onlinePk(OnlinePKReqVO onlinePkReqVO) {
        LivingRoomReqDTO reqDTO = ConvertBeanUtils.convert(onlinePkReqVO,LivingRoomReqDTO.class);
        reqDTO.setAppId(AppIdEnum.LIVE_BIZ.getCode());
        reqDTO.setPkObjId(LiveRequestContext.getUserId());
        LivingPkRespDTO tryOnlineStatus = livingRoomRpc.onlinePK(reqDTO);
        ErrorAssert.isTure(tryOnlineStatus.isOnlineStatus(), new LiveErrorException(-1,tryOnlineStatus.getMsg()));
        return true;
    }

    @Override
    public boolean closeLiving(Integer roomId) {
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setRoomId(roomId);
        livingRoomReqDTO.setAnchorId(LiveRequestContext.getUserId());
        return livingRoomRpc.closeLiving(livingRoomReqDTO);
    }

    @Override
    public LivingRoomInitVO anchorConfig(Long userId, Integer roomId) {
        LivingRoomRespDTO respDTO = livingRoomRpc.queryByRoomId(roomId);
        ErrorAssert.isNotNull(respDTO,ApiErrorEnum.LIVING_ROOM_END);
        Map<Long,UserDTO> userDTOMap = userRpc.batchQueryUserInfo(Arrays.asList(respDTO.getAnchorId(),userId).stream().distinct().collect(Collectors.toList()));
        UserDTO anchor = userDTOMap.get(respDTO.getAnchorId());
        UserDTO watcher = userDTOMap.get(userId);
        LivingRoomInitVO respVO = new LivingRoomInitVO();
        respVO.setAnchorNickName(anchor.getNickName());
        respVO.setWatcherNickName(watcher.getNickName());
        respVO.setUserId(userId);
        //给定一个默认的头像
        respVO.setAvatar(StringUtils.isEmpty(anchor.getAvatar())?"https://s1.ax1x.com/2022/12/18/zb6q6f.png":anchor.getAvatar());
        respVO.setWatcherAvatar(watcher.getAvatar());
        if (respDTO == null || respDTO.getAnchorId() == null || userId == null) {
            //这种就是属于直播间已经不存在的情况了
            respVO.setRoomId(-1);
            return respVO;
        }

        boolean isAuthor = respDTO.getAnchorId().equals(userId);
        respVO.setRoomId(respDTO.getId());
        respVO.setAnchorId(respDTO.getAnchorId());
        respVO.setAnchor(isAuthor);

        if (isAuthor) {
            RedPacketConfigRespDTO redPacketConfigRespDTO = redPacketConfigRpc.queryByAnchorId(userId);
            if (redPacketConfigRespDTO != null) {
                respVO.setRedPacketConfigCode(redPacketConfigRespDTO.getConfigCode());
            }
        }
        respVO.setDefaultBgImg("https://picst.sunbangyan.cn/2023/08/29/waxzj0.png");
        return respVO;
    }

    @Override
    public Boolean prepareRedPacket(Long userId, Integer roomId) {
        LivingRoomRespDTO livingRoomRespDTO = livingRoomRpc.queryByRoomId(roomId);
        ErrorAssert.isNotNull(livingRoomRespDTO, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isTure(userId.equals(livingRoomRespDTO.getAnchorId()), BizBaseErrorEnum.PARAM_ERROR);
        return redPacketConfigRpc.prepareRedPacket(userId);
    }

    @Override
    public Boolean startRedPacket(Long userId, String code) {
        RedPacketConfigReqDTO reqDTO = new RedPacketConfigReqDTO();
        reqDTO.setUserId(userId);
        reqDTO.setRedPacketConfigCode(code);
        LivingRoomRespDTO livingRoomRespDTO = livingRoomRpc.queryByAnchorId(userId);
        ErrorAssert.isNotNull(livingRoomRespDTO, BizBaseErrorEnum.PARAM_ERROR);
        reqDTO.setRoomId(livingRoomRespDTO.getId());
        return redPacketConfigRpc.startRedPacket(reqDTO);
    }


    @Override
    public RedPacketReceiveVO receiveRedPacket(Long userId, String code) {
        RedPacketConfigReqDTO reqDTO = new RedPacketConfigReqDTO();
        reqDTO.setUserId(userId);
        reqDTO.setRedPacketConfigCode(code);
        RedPacketReceiveDTO receiveDTO = redPacketConfigRpc.receiveRedPacket(reqDTO);
        RedPacketReceiveVO respVO = new RedPacketReceiveVO();
        if (receiveDTO == null) {
            respVO.setMsg("红包已派发完毕");
        } else {
            respVO.setPrice(receiveDTO.getPrice());
            respVO.setMsg(receiveDTO.getNotifyMsg());
        }
        return respVO;
    }

}
