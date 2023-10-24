package com.im.flashcomms.common.user.service.adapter;

import com.im.flashcomms.common.common.domain.enums.YesOrNoEnum;
import com.im.flashcomms.common.user.domain.entity.ItemConfig;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.domain.entity.UserBackpack;
import com.im.flashcomms.common.user.domain.vo.resp.user.BadgeResp;
import com.im.flashcomms.common.user.domain.vo.resp.user.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.BeanUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static UserInfoResp buildUserInfo(User user, Integer countByValidItemId) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtils.copyProperties(user,userInfoResp);
        userInfoResp.setModifyName(countByValidItemId);
        return userInfoResp;
    }

    public static List<BadgeResp> buildBadgeResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, User user) {
        Set<Long> collect = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return itemConfigs.stream().map(a -> {
                    BadgeResp badgeResp = new BadgeResp();
                    BeanUtils.copyProperties(a, badgeResp);
                    badgeResp.setObtain(collect.contains(a.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
                    badgeResp.setWearing(Objects.equals(a.getId(), user.getItemId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
                    return badgeResp;
                }).sorted(Comparator.comparing(BadgeResp::getWearing, Comparator.reverseOrder())
                        .thenComparing(BadgeResp::getObtain, Comparator.reverseOrder()))
                .collect(Collectors.toList());

    }
}
