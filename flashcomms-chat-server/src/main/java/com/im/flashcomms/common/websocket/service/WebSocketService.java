package com.im.flashcomms.common.websocket.service;

import io.netty.channel.Channel;
import me.chanjar.weixin.common.error.WxErrorException;

public interface WebSocketService {


    /**
     * 保存channel
     * @param channel
     */
    void connect(Channel channel);


    /**
     * 申请二维码
     * @param channel
     */
    void handleLoginReq(Channel channel) throws WxErrorException;
}
