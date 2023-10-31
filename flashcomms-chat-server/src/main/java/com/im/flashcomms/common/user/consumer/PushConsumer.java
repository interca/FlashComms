package com.im.flashcomms.common.user.consumer;


import cn.hutool.json.JSONUtil;
import com.im.flashcomms.common.common.config.RabbitmqConfig;
import com.im.flashcomms.common.common.domain.dto.LoginMessageDTO;
import com.im.flashcomms.common.common.domain.dto.PushMessageDTO;
import com.im.flashcomms.common.websocket.domain.enums.WSPushTypeEnum;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Description:
 * Author: huj
 * Date: 2023-10-31
 */

@Component
public class PushConsumer   {
    @Autowired
    private WebSocketService webSocketService;

    @RabbitListener(queues = RabbitmqConfig.PUSH_QUEUE)
    @Transactional
    public void onMessage(Message msg, Channel channel) throws IOException {
        String str;
        try {
            str =  new String(msg.getBody());
            PushMessageDTO message = JSONUtil.toBean(str, PushMessageDTO.class);
            WSPushTypeEnum wsPushTypeEnum = WSPushTypeEnum.of(message.getPushType());
            switch (wsPushTypeEnum) {
                case USER:
                    message.getUidList().forEach(uid -> {
                        webSocketService.sendToUid(message.getWsBaseMsg(), uid);
                    });
                    break;
                case ALL:
                    webSocketService.sendToAllOnline(message.getWsBaseMsg(), null);
                    break;
            }
        }catch (Exception e){
            //如果发送异常  消息重新回队列
            channel.basicNack(msg.getMessageProperties().getDeliveryTag(),true,true);
            throw new RuntimeException(e);
        }

    }
}
