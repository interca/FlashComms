package com.im.flashcomms.common.websocket.service.impl;

import com.im.flashcomms.common.websocket.domain.dto.WSChannelExtraDTO;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 专门管理websocket的逻辑
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {


    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    /**
     * 保存channel
     * @param channel
     */
    @Override
    public void connect(Channel channel) {
       ONLINE_WS_MAP.put(channel,new WSChannelExtraDTO());
    }
}
