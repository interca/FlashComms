package com.im.flashcomms.common.common.event.listener;


import com.im.flashcomms.common.common.event.UserApplyEvent;
import com.im.flashcomms.common.user.dao.UserApplyDao;
import com.im.flashcomms.common.user.domain.entity.UserApply;
import com.im.flashcomms.common.user.service.Impl.PushService;
import com.im.flashcomms.common.user.service.adapter.WSAdapter;
import com.im.flashcomms.common.websocket.domain.vo.resp.WSFriendApply;
import com.im.flashcomms.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 好友申请监听器
 *
 * @author HYJ
 */
@Slf4j
@Component
public class UserApplyListener {
    @Autowired
    private UserApplyDao userApplyDao;
    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private PushService pushService;

    @Async
    @TransactionalEventListener(classes = UserApplyEvent.class, fallbackExecution = true)
    public void notifyFriend(UserApplyEvent event) {
        UserApply userApply = event.getUserApply();
        Integer unReadCount = userApplyDao.getUnReadCount(userApply.getTargetId());
        pushService.sendPushMsg(WSAdapter.buildApplySend(new WSFriendApply(userApply.getUid(), unReadCount)), userApply.getTargetId());
    }

}
