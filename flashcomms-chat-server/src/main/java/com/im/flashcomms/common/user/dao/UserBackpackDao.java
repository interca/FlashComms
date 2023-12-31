package com.im.flashcomms.common.user.dao;

import com.im.flashcomms.common.common.domain.enums.YesOrNoEnum;
import com.im.flashcomms.common.user.domain.entity.UserBackpack;
import com.im.flashcomms.common.user.mapper.UserBackpackMapper;
import com.im.flashcomms.common.user.service.IUserBackpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-18
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack>  {

    /**
     * 获取剩余的改名卡
     * @param uid
     * @param itemId
     */
    public Integer getCountByValidItemId(Long uid, Long itemId) {
        Integer count = lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();
        return count;
    }

    public UserBackpack getFirstValidItem(Long uid, Long itemId) {
        UserBackpack one = lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .orderByAsc(UserBackpack::getId)
                .one();
        return one;
    }

    public boolean userItem(UserBackpack firstValidItem) {
        return lambdaUpdate()
                .eq(UserBackpack::getId, firstValidItem.getId())
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .set(UserBackpack::getStatus, YesOrNoEnum.YES.getStatus()).update();
    }

    public List<UserBackpack> getByItemIds(Long uid, List<Long> collect) {
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .in(UserBackpack::getItemId, collect)
                .list();

    }

    public UserBackpack getByIdempotent(String idempotent) {
        return lambdaQuery()
                .eq(UserBackpack::getIdempotent, idempotent)
                .one();
    }


    public List<UserBackpack> getByItemIds(List<Long> uids, List<Long> itemIds) {
        return lambdaQuery().in(UserBackpack::getUid, uids)
                .in(UserBackpack::getItemId, itemIds)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .list();
    }

    public UserBackpack getByIdp(String idempotent) {
        return lambdaQuery().eq(UserBackpack::getIdempotent, idempotent).one();
    }
}
