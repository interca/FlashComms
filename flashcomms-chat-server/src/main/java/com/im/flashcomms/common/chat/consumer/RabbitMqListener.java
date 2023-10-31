package com.im.flashcomms.common.chat.consumer;


import cn.hutool.json.JSONUtil;

import com.im.flashcomms.common.chat.dao.ContactDao;
import com.im.flashcomms.common.chat.dao.MessageDao;
import com.im.flashcomms.common.chat.dao.RoomDao;
import com.im.flashcomms.common.chat.dao.RoomFriendDao;
import com.im.flashcomms.common.chat.domain.entity.Room;
import com.im.flashcomms.common.chat.domain.entity.RoomFriend;
import com.im.flashcomms.common.chat.domain.enums.RoomTypeEnum;
import com.im.flashcomms.common.chat.domain.vo.response.ChatMessageResp;
import com.im.flashcomms.common.chat.service.ChatService;
import com.im.flashcomms.common.chat.service.WeChatMsgOperationService;
import com.im.flashcomms.common.chat.service.cache.GroupMemberCache;
import com.im.flashcomms.common.chat.service.cache.HotRoomCache;
import com.im.flashcomms.common.chat.service.cache.RoomCache;
import com.im.flashcomms.common.common.domain.dto.MsgSendMessageDTO;
import com.im.flashcomms.common.user.cache.UserCache;
import com.im.flashcomms.common.user.service.Impl.PushService;
import com.im.flashcomms.common.user.service.adapter.WSAdapter;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


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
    public  void robTicket(Message msg, Channel channel) throws Exception {
        String str ;
        try {
            //接受消息
            str =  new String(msg.getBody());
            MsgSendMessageDTO dto = JSONUtil.toBean(str, MsgSendMessageDTO.class);
            com.im.flashcomms.common.chat.domain.entity.Message message = messageDao.getById(dto.getMsgId());
            Room room = roomCache.get(message.getRoomId());
            ChatMessageResp msgResp = chatService.getMsgResp(message, null);
            //所有房间更新房间最新消息
            roomDao.refreshActiveTime(room.getId(), message.getId(), message.getCreateTime());
            roomCache.delete(room.getId());
            if (room.isHotRoom()) {//热门群聊推送所有在线的人
                //更新热门群聊时间-redis
                hotRoomCache.refreshActiveTime(room.getId(), message.getCreateTime());
                //推送所有人
                pushService.sendPushMsg(WSAdapter.buildMsgSend(msgResp));
            } else {
                List<Long> memberUidList = new ArrayList<>();
                if (Objects.equals(room.getType(), RoomTypeEnum.GROUP.getType())) {//普通群聊推送所有群成员
                    memberUidList = groupMemberCache.getMemberUidList(room.getId());
                } else if (Objects.equals(room.getType(), RoomTypeEnum.FRIEND.getType())) {//单聊对象
                    //对单人推送
                    RoomFriend roomFriend = roomFriendDao.getByRoomId(room.getId());
                    memberUidList = Arrays.asList(roomFriend.getUid1(), roomFriend.getUid2());
                }
                //更新所有群成员的会话时间
                contactDao.refreshOrCreateActiveTime(room.getId(), memberUidList, message.getId(), message.getCreateTime());
                //推送房间成员
                pushService.sendPushMsg(WSAdapter.buildMsgSend(msgResp), memberUidList);
            }
        } catch (Exception e) {
            //如果发送异常  消息重新回队列
            channel.basicNack(msg.getMessageProperties().getDeliveryTag(),true,true);
            throw new RuntimeException(e);
        }
    }


}
