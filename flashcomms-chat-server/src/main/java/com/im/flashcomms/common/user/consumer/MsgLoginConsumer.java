package com.im.flashcomms.common.user.consumer;


import cn.hutool.json.JSONUtil;
import com.im.flashcomms.common.common.config.RabbitmqConfig;
import com.im.flashcomms.common.common.domain.dto.LoginMessageDTO;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Description: 在本地服务上找寻对应channel，将对应用户登陆，并触发所有用户收到上线事件
 * Author: hyj
 * Date: 2023-10-31
 */


@Component
public class MsgLoginConsumer{
    @Autowired
    private WebSocketService webSocketService;

    @RabbitListener(queues = RabbitmqConfig.LOGIN_MSG_QUEUE)
    @Transactional
    public void onMessage(Message msg, Channel channel) throws IOException {
        String str;
        try {
            str =  new String(msg.getBody());
            LoginMessageDTO loginMessageDTO = JSONUtil.toBean(str, LoginMessageDTO.class);
            //尝试登录登录
            webSocketService.scanLoginSuccess(loginMessageDTO.getCode(), loginMessageDTO.getUid());
        }catch (Exception e){
            //如果发送异常  消息重新回队列
            channel.basicNack(msg.getMessageProperties().getDeliveryTag(),true,true);
            throw new RuntimeException(e);
        }
    }

}
