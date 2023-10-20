package com.im.flashcomms.common.common.event;

import com.im.flashcomms.common.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户注册事件
 */

@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private User user;
    public UserRegisterEvent(Object source,User user) {
        super(source);
        this.user = user;
    }
}
