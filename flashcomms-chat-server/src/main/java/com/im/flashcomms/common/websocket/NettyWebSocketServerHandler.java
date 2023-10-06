package com.im.flashcomms.common.websocket;

import cn.hutool.json.JSONUtil;
import com.im.flashcomms.common.websocket.domain.enums.WSReqTypeEnum;
import com.im.flashcomms.common.websocket.domain.vo.req.WSBaseReq;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 自定义业务处理
 *
 */
//处理器共用
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())){
            case LOGIN:
                break;
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
        }
    }
}
