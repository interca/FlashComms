package com.im.flashcomms.common.user.service.adapter;

import com.im.flashcomms.common.user.domain.entity.User;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

public class UserAdapter {
    public static User buildUserSave(String openId) {
        return User.builder().openId(openId).build();
    }

    public static User buildAuthorizeUser(Long id, WxOAuth2UserInfo userInfo) {
        User user = new User();
        user.setId(id);
        user.setName(userInfo.getNickname());
        user.setAvatar(userInfo.getHeadImgUrl());
        return user;
    }
}
