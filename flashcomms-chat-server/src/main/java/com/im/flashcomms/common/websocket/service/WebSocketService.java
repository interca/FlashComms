package com.im.flashcomms.common.websocket.service;

import com.im.flashcomms.common.websocket.domain.vo.resp.WSBaseResp;
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
     * 扫码成功等待用户授权
     * @param code
     */
    void waitAuthorize(Integer code);

    /**
     * 扫码登陆成功
     * @param code
     * @param id
     */
    void scanLoginSuccess(Integer code, Long id);


    /**
     * 用户尝试用token重连
     * @param channel
     * @param data
     */
    void authorize(Channel channel, String data);

    /**
     * 发消息给全部用户
     */
    void sendMsgToAll(WSBaseResp<?> wsBaseResp);
}
