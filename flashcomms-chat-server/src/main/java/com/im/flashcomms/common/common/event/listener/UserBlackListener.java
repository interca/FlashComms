package com.im.flashcomms.common.common.event.listener;

import com.im.flashcomms.common.common.event.UserBlackEvent;
import com.im.flashcomms.common.common.event.UserRegisterEvent;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.domain.enums.IdempotentEnum;
import com.im.flashcomms.common.user.domain.enums.ItemEnum;
import com.im.flashcomms.common.user.service.IUserBackpackService;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import com.im.flashcomms.common.websocket.service.adapter.WebSocketAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 用户黑名单监听
 */
@Component
public class UserBlackListener {


    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private UserDao userDao;

    /**
     *
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void sendMsg(UserBlackEvent event){
        User user = event.getUser();
        webSocketService.sendMsgToAll(WebSocketAdapter.buildBlack(user));
    }


    /**
     * 把用户拉黑
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void changeUserStatus(UserBlackEvent event){
        userDao.invalidUid(event.getUser().getId());
    }

}
