package com.logilong.live.im.core.server.handler.tcp;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import com.logilong.live.im.core.server.common.ImContextUtils;
import com.logilong.live.im.core.server.common.ImMsg;
import com.logilong.live.im.core.server.handler.ImHandlerFactory;
import com.logilong.live.im.core.server.handler.impl.LogoutMsgHandler;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * im消息统一handler入口
 */
@Component
@ChannelHandler.Sharable
public class TcpImServerCoreHandler extends SimpleChannelInboundHandler<ImMsg> {

    @Resource
    private ImHandlerFactory imHandlerFactory;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private LogoutMsgHandler logoutMsgHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImMsg msg) throws Exception {
        imHandlerFactory.doMsgHandler(ctx, msg);
    }

    /**
     * 正常或者意外断线，都会触发到这里
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId != null && appId != null) {
            logoutMsgHandler.logoutHandler(ctx,userId,appId);
        }
    }
}
