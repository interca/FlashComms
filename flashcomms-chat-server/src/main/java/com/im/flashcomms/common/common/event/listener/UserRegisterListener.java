package com.im.flashcomms.common.common.event.listener;

import com.im.flashcomms.common.common.event.UserRegisterEvent;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.domain.enums.IdempotentEnum;
import com.im.flashcomms.common.user.domain.enums.ItemEnum;
import com.im.flashcomms.common.user.service.IUserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 用户事件监听
 */
@Component
public class UserRegisterListener {

    @Autowired
    private IUserBackpackService userBackpackService;


    @Autowired
    private UserDao userDao;

    /**
     * 在事物结束后才执行
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class)
    public void sendCard(UserRegisterEvent event){
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID,
                user.getId().toString()
                );
    }

    /**
     * 在事物结束后才执行
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class)
    public void sendBadge(UserRegisterEvent event){
        User user = event.getUser();
        //前100名给一个徽章
        int count = userDao.count();
        if(count < 10) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID,
                    user.getId().toString()
            );
        }else if(count < 100){
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID,
                    user.getId().toString()
            );
        }
    }

}
