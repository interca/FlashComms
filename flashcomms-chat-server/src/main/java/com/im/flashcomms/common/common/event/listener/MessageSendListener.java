package com.im.flashcomms.common.common.event.listener;

import com.im.flashcomms.common.chat.dao.ContactDao;
import com.im.flashcomms.common.chat.dao.MessageDao;
import com.im.flashcomms.common.chat.dao.RoomDao;
import com.im.flashcomms.common.chat.dao.RoomFriendDao;
import com.im.flashcomms.common.chat.domain.entity.Message;
import com.im.flashcomms.common.chat.domain.entity.Room;
import com.im.flashcomms.common.chat.domain.enums.HotFlagEnum;
import com.im.flashcomms.common.chat.service.ChatService;
import com.im.flashcomms.common.chat.service.WeChatMsgOperationService;
import com.im.flashcomms.common.chat.service.cache.GroupMemberCache;
import com.im.flashcomms.common.chat.service.cache.HotRoomCache;
import com.im.flashcomms.common.chat.service.cache.RoomCache;

import com.im.flashcomms.common.common.config.RabbitmqConfig;
import com.im.flashcomms.common.common.domain.dto.MsgSendMessageDTO;
import com.im.flashcomms.common.common.event.MessageSendEvent;
import com.im.flashcomms.common.user.cache.UserCache;
import com.im.flashcomms.transaction.service.MQProducer;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

/**
 * 消息发送监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class MessageSendListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageDao messageDao;


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
    private MQProducer mqProducer;


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Long msgId = event.getMsgId();
        mqProducer.sendSecureMsg(RabbitmqConfig.FLASHCOMMS_MSG_EXCHANGE, new MsgSendMessageDTO(msgId), "flashcomms");
    }



    public boolean isHotRoom(Room room) {
        return Objects.equals(HotFlagEnum.YES.getType(), room.getHotFlag());
    }


}
