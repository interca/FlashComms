package com.im.flashcomms.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.im.flashcomms.common.websocket.domain.dto.WSChannelExtraDTO;
import com.im.flashcomms.common.websocket.domain.enums.WSRespTypeEnum;
import com.im.flashcomms.common.websocket.domain.vo.resp.WSBaseResp;
import com.im.flashcomms.common.websocket.domain.vo.resp.WSLoginUrl;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import com.im.flashcomms.common.websocket.service.adapter.WebSocketAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.tomcat.websocket.WsRemoteEndpointAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 专门管理websocket的逻辑
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private WxMpService wxMpService;

    private static int  MAXIMUM_SIZE = 1000;

    /**
     * 关联用户连接(登陆态/游客)
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();


    /**
     * 保存二维码和channel 并且设置过期时间和最多连接数
     */
    private static final Cache<Integer,Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterAccess(Duration.ofHours(1))
            .build();




    /**
     * 保存channel
     * @param channel
     */
    @Override
    public void connect(Channel channel) {
       ONLINE_WS_MAP.put(channel,new WSChannelExtraDTO());
    }


    /**
     * 申请二维码
     * @param channel
     */
    @Override
    public void handleLoginReq(Channel channel) throws WxErrorException {
        //生成随机二维码并且关联channel
        Integer code = generateLoginCode(channel);
        //生成二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) Duration.ofHours(1).getSeconds());
        //推送回前端
        sendMsg(channel, WebSocketAdapter.buildResp(wxMpQrCodeTicket));
    }

    /**
     * 推送信息
     * @param channel
     * @param resp
     */
    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }


    /**
     * 二维码关联channel
     * @param channel
     * @return
     */
    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do{
            code = RandomUtil.randomInt(Integer.MIN_VALUE);
        }while (Objects.isNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code,channel)));
        return code;
    }
}
