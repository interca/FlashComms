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


    /**
     * channel断开连接
     * @param channel
     */
    void offLine(Channel channel);


    /**
     * 扫码登陆成功
     * @param code
     * @param id
     */
    void scanLoginSuccess(Integer code, Long id);
}
