package com.im.flashcomms.common.user.service.adapter;

import com.im.flashcomms.common.user.domain.entity.User;

public class UserAdapter {
    public static User buildUserSave(String openId) {
        return User.builder().openId(openId).build();
    }
}
