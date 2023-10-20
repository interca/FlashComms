package com.im.flashcomms.common.common.event.listener;

import com.im.flashcomms.common.common.event.UserRegisterEvent;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.domain.enums.IdempotentEnum;
import com.im.flashcomms.common.user.domain.enums.ItemEnum;
import com.im.flashcomms.common.user.domain.enums.ItemTypeEnum;
import com.im.flashcomms.common.user.service.IUserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 用户事件监听
 */
@Component
public class UserRegisterListener {

    @Autowired
    private IUserBackpackService userBackpackService;

    @EventListener(classes = UserRegisterEvent.class)
    public void sendCard(UserRegisterEvent event){
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID,
                user.getId().toString()
                );
    }

}
