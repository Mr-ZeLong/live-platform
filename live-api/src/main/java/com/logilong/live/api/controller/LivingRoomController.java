package com.logilong.live.api.controller;

import jakarta.annotation.Resource;
import com.logilong.live.api.error.ApiErrorEnum;
import com.logilong.live.api.service.ILivingRoomService;
import com.logilong.live.api.vo.LivingRoomInitVO;
import com.logilong.live.api.vo.req.LivingRoomReqVO;
import com.logilong.live.api.vo.req.OnlinePKReqVO;
import com.logilong.live.common.interfaces.vo.WebResponseVO;
import com.logilong.live.web.starter.config.RequestLimit;
import com.logilong.live.web.starter.context.LiveRequestContext;
import com.logilong.live.web.starter.error.BizBaseErrorEnum;
import com.logilong.live.web.starter.error.ErrorAssert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/living")
public class LivingRoomController {

    @Resource
    private ILivingRoomService livingRoomService;

    @PostMapping("/list")
    public WebResponseVO list(LivingRoomReqVO livingRoomReqVO) {
        ErrorAssert.isTure(livingRoomReqVO != null && livingRoomReqVO.getType() != null, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isTure(livingRoomReqVO.getPage() > 0 && livingRoomReqVO.getPageSize() <= 100, BizBaseErrorEnum.PARAM_ERROR);
        return WebResponseVO.success(livingRoomService.list(livingRoomReqVO));
    }

    @RequestLimit(limit = 1, second = 10, msg = "开播请求过于频繁，请稍后再试")//[10.4] 限流组件的实现
    @PostMapping("/startingLiving")
    public WebResponseVO startingLiving(Integer type) {
        ErrorAssert.isNotNull(type, BizBaseErrorEnum.PARAM_ERROR);
        Integer roomId = livingRoomService.startingLiving(type);
        LivingRoomInitVO initVO = new LivingRoomInitVO();
        initVO.setRoomId(roomId);
        return WebResponseVO.success(initVO);
    }

    @PostMapping("/onlinePk")
    @RequestLimit(limit = 1, second = 3)
    public WebResponseVO onlinePk(OnlinePKReqVO onlinePkReqVO) {
        ErrorAssert.isNotNull(onlinePkReqVO.getRoomId(), BizBaseErrorEnum.PARAM_ERROR);
        return WebResponseVO.success(livingRoomService.onlinePk(onlinePkReqVO));
    }

    @RequestLimit(limit = 1, second = 10, msg = "关播请求过于频繁，请稍后再试")
    @PostMapping("/closeLiving")
    public WebResponseVO closeLiving(Integer roomId) {
        ErrorAssert.isNotNull(roomId, BizBaseErrorEnum.PARAM_ERROR);
        boolean closeStatus = livingRoomService.closeLiving(roomId);
        if (closeStatus) {
            return WebResponseVO.success();
        }
        return WebResponseVO.bizError("关播异常");
    }

    /**
     * 获取主播相关配置信息（只有主播才会有权限）
     *
     * @return
     */
    @PostMapping("/anchorConfig")
    public WebResponseVO anchorConfig(Integer roomId) {
        return WebResponseVO.success(livingRoomService.anchorConfig(LiveRequestContext.getUserId(), roomId));
    }

    /**
     * 准备生成红包雨数据
     *
     * @return
     */
    @PostMapping("/prepareRedPacket")
    @RequestLimit(limit = 1, second = 10, msg = "正在初始化中，请稍等")
    public WebResponseVO prepareRedPacket(LivingRoomReqVO livingRoomReqVO) {
        return WebResponseVO.success(livingRoomService.prepareRedPacket(LiveRequestContext.getUserId(), livingRoomReqVO.getRoomId()));
    }

    /**
     * 开始红包雨活动，广播直播间用户，开始抢红包
     *
     * @return
     */
    @PostMapping("/startRedPacket")
    @RequestLimit(limit = 1, second = 10, msg = "正在广播直播间用户，请稍等")
    public WebResponseVO startRedPacket(Long userId, String code) {
        return WebResponseVO.success(livingRoomService.startRedPacket(LiveRequestContext.getUserId(), code));
    }

    /**
     * 领取红包
     *
     * @return
     */
    @RequestLimit(limit = 1, second = 1, msg = "")
    @PostMapping("/receiveRedPacket")
    public WebResponseVO receiveRedPacket(LivingRoomReqVO livingRoomReqVO) {
        return WebResponseVO.success(livingRoomService.receiveRedPacket(LiveRequestContext.getUserId(), livingRoomReqVO.getRedPacketConfigCode()));
    }

}
