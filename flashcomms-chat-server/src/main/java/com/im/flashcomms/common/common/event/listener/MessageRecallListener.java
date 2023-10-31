package com.im.flashcomms.common.common.event.listener;

import com.im.flashcomms.common.chat.domain.dto.ChatMsgRecallDTO;
import com.im.flashcomms.common.chat.service.ChatService;
import com.im.flashcomms.common.chat.service.cache.MsgCache;
import com.im.flashcomms.common.common.event.MessageRecallEvent;
import com.im.flashcomms.common.user.service.Impl.PushService;
import com.im.flashcomms.common.user.service.adapter.WSAdapter;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 消息撤回监听器
 *
 * @author HYJ
 */
@Slf4j
@Component
public class MessageRecallListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MsgCache msgCache;
    @Autowired
    private PushService pushService;

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void evictMsg(MessageRecallEvent event) {
        ChatMsgRecallDTO recallDTO = event.getRecallDTO();
        msgCache.evictMsg(recallDTO.getMsgId());
    }

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void sendToAll(MessageRecallEvent event) {
        pushService.sendPushMsg(WSAdapter.buildMsgRecall(event.getRecallDTO()));
    }

}
