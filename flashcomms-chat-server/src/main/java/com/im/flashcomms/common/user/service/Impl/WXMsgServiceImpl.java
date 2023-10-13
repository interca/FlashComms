package com.im.flashcomms.common.user.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.service.UserService;
import com.im.flashcomms.common.user.service.WXMsgService;
import com.im.flashcomms.common.user.service.adapter.TextBuilder;
import com.im.flashcomms.common.user.service.adapter.UserAdapter;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.sql.Struct;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 微信信息处理类
 */
@Slf4j
@Service
public class WXMsgServiceImpl implements WXMsgService {

    /**
     * openid和登录code的关系
     */
    private static  final ConcurrentHashMap<String,Integer>WAIT_AUTHORIZE_MAP = new ConcurrentHashMap<>();


    @Autowired
    private UserDao userDao;


    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketService webSocketService;

    @Value("${wx.mp.callback}")
    private String callback;

    @Autowired
    @Lazy
    private WxMpService wxMpService;
    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";


    /**
     * 扫码事件
     * @param wxMpXmlMessage
     */
    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        Integer code = getEventKey(wxMpXmlMessage);
        String openId = wxMpXmlMessage.getFromUser();
        if(Objects.isNull(code))return null;
        User user = userDao.getByOpenId(openId);
        boolean registered = Objects.nonNull(user);
        boolean authorized =registered && StrUtil.isNotBlank(user.getAvatar());
        //用户已经注册并且授权，也就是用户之前登陆过了，不用再点登陆了
        if(registered && authorized){
            //登陆成功的逻辑  通过code找到channel推送消息
            webSocketService.scanLoginSuccess(code,user.getId());
            return null;
        }else{
            User  insert =  UserAdapter.buildUserSave(openId);
            userService.register(insert);
        }
        WAIT_AUTHORIZE_MAP.put(openId,code);
        //授权中要通知用户
        webSocketService.waitAuthorize(code);
        String authorizeUrl= String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        // 扫码事件处理
        return TextBuilder.build("请点击链接授权：<a href=\"" + authorizeUrl + "\">登录</a>",wxMpXmlMessage);
    }


    /**
     * 用户授权(第一次登陆)
     * @param userInfo
     */
    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userDao.getByOpenId(openid);
        if(StrUtil.isBlank(user.getAvatar())){
            fillUserInfo(user.getId(),userInfo);
        }
        //通过code获取channel进行登陆
        Integer code = WAIT_AUTHORIZE_MAP.remove(openid);
        webSocketService.scanLoginSuccess(code,user.getId());
    }

    private void fillUserInfo(Long id, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(id, userInfo);
        userDao.updateById(user);
    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        String eventKey ;
        String code;
        try{
            eventKey = wxMpXmlMessage.getEventKey();
            //时间码，通过关注点的
            code = eventKey.replace("grscene_","");
        }catch (Exception e){
            log.error("{}",wxMpXmlMessage.getEventKey());
            return null;
        }
        return Integer.parseInt(code);
    }
}
