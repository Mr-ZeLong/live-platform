package com.logilong.live.api.controller;

import jakarta.annotation.Resource;
import com.logilong.live.api.service.IGiftService;
import com.logilong.live.api.vo.req.GiftReqVO;
import com.logilong.live.api.vo.resp.GiftConfigVO;
import com.logilong.live.common.interfaces.vo.WebResponseVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/gift")
public class GiftController {

    @Resource
    private IGiftService giftService;

    /**
     * 获取礼物列表
     *
     * @return
     */
    @PostMapping("/listGift")
    public WebResponseVO listGift() {
        //调用rpc的方法，检索出来礼物配置列表
        List<GiftConfigVO> giftConfigVOS = giftService.listGift();
        return WebResponseVO.success(giftConfigVOS);
    }

    /**
     * 发送礼物方法
     * 具体实现在后边的章节会深入讲解
     *
     * @return
     */
    @PostMapping("/send")
    public WebResponseVO send(GiftReqVO giftReqVO) {
        return WebResponseVO.success(giftService.send(giftReqVO));
    }

}
