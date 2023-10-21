package com.im.flashcomms.common.common.event;

import com.im.flashcomms.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户拉黑事件
 */

@Getter
public class UserBlackEvent extends ApplicationEvent {
    private User user;
    public UserBlackEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
