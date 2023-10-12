package com.im.flashcomms.common.user.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.service.UserService;
import com.im.flashcomms.common.user.service.WXMsgService;
import com.im.flashcomms.common.user.service.adapter.TextBuilder;
import com.im.flashcomms.common.user.service.adapter.UserAdapter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Objects;


/**
 * 微信信息处理类
 */
@Slf4j
@Service
public class WXMsgServiceImpl implements WXMsgService {

    @Autowired
    private UserDao userDao;


    @Autowired
    private UserService userService;

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
        String code = getEventKey(wxMpXmlMessage);
        String openId = wxMpXmlMessage.getFromUser();
        if(Objects.isNull(code))return null;
        User user = userDao.getByOpenId(openId);
        boolean registered = Objects.nonNull(user);
        boolean authorized = StrUtil.isNotBlank(user.getAvatar());
        //用户已经注册并且授权
        if(registered && authorized){
            //登陆成功
        }else{
            User  insert =  UserAdapter.buildUserSave(openId);
            userService.register(insert);
        }
        String authorizeUrl= String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        // 扫码事件处理
        return TextBuilder.build("请点击链接授权：<a href=\"" + authorizeUrl + "\">登录</a>",wxMpXmlMessage);
    }

    private String getEventKey(WxMpXmlMessage wxMpXmlMessage) {
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
        return code;
    }
}
