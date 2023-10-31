package com.im.flashcomms.common.chat.consumer;


import cn.hutool.json.JSONUtil;

import com.im.flashcomms.common.chat.dao.ContactDao;
import com.im.flashcomms.common.chat.dao.MessageDao;
import com.im.flashcomms.common.chat.dao.RoomDao;
import com.im.flashcomms.common.chat.dao.RoomFriendDao;
import com.im.flashcomms.common.chat.service.ChatService;
import com.im.flashcomms.common.chat.service.WeChatMsgOperationService;
import com.im.flashcomms.common.chat.service.cache.GroupMemberCache;
import com.im.flashcomms.common.chat.service.cache.HotRoomCache;
import com.im.flashcomms.common.chat.service.cache.RoomCache;
import com.im.flashcomms.common.common.domain.dto.MsgSendMessageDTO;
import com.im.flashcomms.common.user.cache.UserCache;
import com.im.flashcomms.common.user.service.Impl.PushService;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * rabnbitmq监听类
 * @author  hyj
 * @since  2022-12-15
 */
@Component
public class RabbitMqListener {

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageDao messageDao;

    @Autowired
    WeChatMsgOperationService weChatMsgOperationService;
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private UserCache userCache;
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private HotRoomCache hotRoomCache;

    @Autowired
    private PushService pushService;


    /**
     * 监听发送消息
     */
    @RabbitListener(queues = "flascomms-msg-queue")
    @Transactional
    public  void robTicket(Message message, Channel channel) throws Exception {
        String str ;
        try {
            //接受消息
            str =  new String(message.getBody());
            MsgSendMessageDTO msgSendMessageDTO = JSONUtil.toBean(str, MsgSendMessageDTO.class);
            Long msgId = msgSendMessageDTO.getMsgId();
        } catch (Exception e) {
            //如果发送异常  消息重新回队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
            throw new RuntimeException(e);
        }
    }


}
