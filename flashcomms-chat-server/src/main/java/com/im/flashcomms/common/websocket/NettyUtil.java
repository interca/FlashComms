package com.im.flashcomms.common.websocket;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * netty工具类
 */
public class NettyUtil {

    public static final AttributeKey<String> IP = AttributeKey.valueOf("ip") ;
    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");

    public  static <T>void setAttr(Channel channel, AttributeKey<T>key,T value){
        Attribute<T> attr = channel.attr(key);
        attr.set(value);
    }

    public static <T> T getAttr(Channel channel,AttributeKey<T> key){
        Attribute<T> attr = channel.attr(key);
        return attr.get();
    }
}
