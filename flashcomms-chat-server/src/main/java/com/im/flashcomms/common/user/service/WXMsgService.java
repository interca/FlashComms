package com.im.flashcomms.common.user.service;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * 微信信息处理类
 */
public interface WXMsgService  {

    /**
     * 扫码事件
     * @param wxMpXmlMessage
     */
    WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage);
}
