package com.im.flashcomms.common.user.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.flashcomms.common.user.domain.entity.UserApply;
import com.im.flashcomms.common.user.domain.enums.ApplyStatusEnum;
import com.im.flashcomms.common.user.domain.enums.ApplyTypeEnum;
import com.im.flashcomms.common.user.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.im.flashcomms.common.user.domain.enums.ApplyReadStatusEnum.READ;
import static com.im.flashcomms.common.user.domain.enums.ApplyReadStatusEnum.UNREAD;
import static com.im.flashcomms.common.user.domain.enums.ApplyStatusEnum.AGREE;


/**
 * <p>
 * 用户申请表 服务实现类
 * </p>
 *
 * @author hyj
 * @since 2023-07-16
 */
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> {

    public UserApply getFriendApproving(Long uid, Long targetUid) {
        return lambdaQuery().eq(UserApply::getUid, uid)
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_APPROVAL)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .one();
    }

    public Integer getUnReadCount(Long targetId) {
        return lambdaQuery().eq(UserApply::getTargetId, targetId)
                .eq(UserApply::getReadStatus, UNREAD.getCode())
                .count();
    }

    public IPage<UserApply> friendApplyPage(Long uid, Page page) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .orderByDesc(UserApply::getCreateTime)
                .page(page);
    }

    public void readApples(Long uid, List<Long> applyIds) {
        lambdaUpdate()
                .set(UserApply::getReadStatus, READ.getCode())
                .eq(UserApply::getReadStatus, UNREAD.getCode())
                .in(UserApply::getId, applyIds)
                .eq(UserApply::getTargetId, uid)
                .update();
    }

    public void agree(Long applyId) {
        lambdaUpdate().set(UserApply::getStatus, AGREE.getCode())
                .eq(UserApply::getId, applyId)
                .update();
    }
}
