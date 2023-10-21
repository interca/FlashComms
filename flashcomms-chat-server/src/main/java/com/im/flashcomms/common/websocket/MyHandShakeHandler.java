package com.im.flashcomms.common.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Optional;


/**
 * 握手连接的时候拿出token
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-09-03
 */
public class MyHandShakeHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       if(msg instanceof FullHttpRequest){
           FullHttpRequest request = (FullHttpRequest) msg;
           UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());
           Optional<String> tokenOptional = Optional.ofNullable(urlBuilder)
                   .map(UrlBuilder::getQuery)
                   .map(k -> k.get("token"))
                   .map(CharSequence::toString);
          if(tokenOptional.isPresent()){
               NettyUtil.setAttr(ctx.channel(),NettyUtil.TOKEN,tokenOptional.get());
               //替换成不带参数的路径
               request.setUri(urlBuilder.getPath().toString());
          }
          //获取用户Ip
           String ip = request.headers().get("X-Real-IP");
           if(StringUtils.isBlank(ip)){
               InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
               ip = address.getAddress().getHostAddress();
           }
           //保存在channel附件
           NettyUtil.setAttr(ctx.channel(),NettyUtil.IP,ip);
           ctx.pipeline().remove(this);
       }
       ctx.fireChannelRead(msg);
    }
}
