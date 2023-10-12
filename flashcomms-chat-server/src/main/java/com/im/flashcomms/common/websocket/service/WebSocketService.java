package com.im.flashcomms.common.websocket.service;

import io.netty.channel.Channel;

public interface WebSocketService {


    /**
     * 保存channel
     * @param channel
     */
    void connect(Channel channel);
}
