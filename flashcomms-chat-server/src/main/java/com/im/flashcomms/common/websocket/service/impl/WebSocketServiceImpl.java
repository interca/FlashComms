package com.im.flashcomms.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.im.flashcomms.common.common.event.UserOnlineEvent;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.domain.enums.RoleEnum;
import com.im.flashcomms.common.user.service.IRoleService;
import com.im.flashcomms.common.user.service.LoginService;
import com.im.flashcomms.common.websocket.NettyUtil;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 专门管理websocket的逻辑
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private IRoleService roleService;


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;



    private final static int  MAXIMUM_SIZE = 1000;

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
     * 用户下线统一处理
     * @param channel
     */
    @Override
    public void offLine(Channel channel) {
        //移除channel
        ONLINE_WS_MAP.remove(channel);

        //todo 用户下线
    }

    /**
     * 扫码成功等待用户授权
     * @param code
     */
    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        sendMsg(channel,WebSocketAdapter.buildWaitAuthorizeResp());
    }


    /**
     * 扫码登陆成功
     * @param code
     * @param id
     */
    @Override
    public void scanLoginSuccess(Integer code, Long id) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if(Objects.isNull(channel)){
            return;
        }
        User user = userDao.getById(id);
        //移除code
        WAIT_LOGIN_MAP.asMap().remove(code);
        //获取token
        String token = loginService.login(id);
        loginSuccess(channel,user,token);
    }



    /**
     * 用户尝试用token重连
     * @param channel
     * @param token
     */
    @Override
    public void authorize(Channel channel, String token) {
        Long uid = loginService.getValidUid(token);
        if(Objects.nonNull(uid)){
            //登录成功
            User user = userDao.getById(uid);
            loginSuccess(channel,user,token);
        }else {
            //告诉前端要去除token
            sendMsg(channel,WebSocketAdapter.buildInvalidTokenResp());
        }
    }

    @Override
    public void sendMsgToAll(WSBaseResp<?> wsBaseResp) {
        ONLINE_WS_MAP.forEach((channel, wsChannelExtraDTO) -> {
            threadPoolTaskExecutor.execute(()->{
                 sendMsg(channel,wsBaseResp);
            });
        });
    }

    /**
     * 用户上线
     * @param channel
     * @param user
     * @param token
     */
    private void loginSuccess(Channel channel, User user, String token) {
        //保存channel对应的uid
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        sendMsg(channel,WebSocketAdapter.buildResp(user,token,roleService.hasPower(user.getId(), RoleEnum.CHAT_MANAGER)));
        user.setLastOptTime(LocalDateTime.now());
        user.refreshIp(NettyUtil.getAttr(channel,NettyUtil.IP));
        //发布用户上线成功的事件
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this,user));
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
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        }while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code,channel)));
        return code;
    }
}
