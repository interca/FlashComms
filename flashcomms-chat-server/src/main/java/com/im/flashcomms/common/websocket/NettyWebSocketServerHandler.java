package com.im.flashcomms.common.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.im.flashcomms.common.websocket.domain.enums.WSReqTypeEnum;
import com.im.flashcomms.common.websocket.domain.vo.req.WSBaseReq;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义业务处理
 *
 */
//处理器共用
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Autowired
    private WebSocketService webSocketService;




    /**
     * 连接建立
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        webSocketService.connect(ctx.channel());
    }

    /**
     * 心跳检查
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 关闭用户的连接
                System.out.println("用户下线");
                ctx.channel().close();
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
             System.out.println("握手成功");
        }
        //super.userEventTriggered(ctx, evt);
    }






    /**
     * 读取事件
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())){
            case LOGIN:
                ctx.channel().writeAndFlush(new TextWebSocketFrame("123"));
                break;
            case AUTHORIZE:
                webSocketService.handleLoginReq(ctx.channel());
                break;
            case HEARTBEAT:
                break;
        }
    }
}
