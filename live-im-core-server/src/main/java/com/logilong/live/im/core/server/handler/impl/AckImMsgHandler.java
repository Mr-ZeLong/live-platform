package com.logilong.live.im.core.server.handler.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import com.logilong.live.im.core.server.common.ImContextUtils;
import com.logilong.live.im.core.server.common.ImMsg;
import com.logilong.live.im.core.server.handler.SimplyHandler;
import com.logilong.live.im.core.server.service.IMsgAckCheckService;
import com.logilong.live.im.dto.ImMsgBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 登出消息的处理逻辑统一收拢到这个类中
 */
@Component
public class AckImMsgHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AckImMsgHandler.class);

    @Resource
    private IMsgAckCheckService msgAckCheckService;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appid = ImContextUtils.getAppId(ctx);
        if (userId == null && appid == null) {
            ctx.close();
            throw new IllegalArgumentException("attr is error");
        }
        msgAckCheckService.doMsgAck(JSON.parseObject(imMsg.getBody(), ImMsgBody.class));
    }
}
