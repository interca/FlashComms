package com.im.flashcomms.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.flashcomms.common.chat.domain.entity.Message;
import com.im.flashcomms.common.chat.domain.enums.MessageStatusEnum;
import com.im.flashcomms.common.chat.mapper.MessageMapper;
import com.im.flashcomms.common.common.domain.vo.req.CursorPageBaseReq;
import com.im.flashcomms.common.common.domain.vo.resp.CursorPageBaseResp;
import com.im.flashcomms.common.common.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-03-25
 */
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message> {

    public CursorPageBaseResp<Message> getCursorPage(Long roomId, CursorPageBaseReq request, Long lastMsgId) {
        return CursorUtils.getCursorPageByMysql(this, request, wrapper -> {
            wrapper.eq(Message::getRoomId, roomId);
            wrapper.eq(Message::getStatus, MessageStatusEnum.NORMAL.getStatus());
            wrapper.le(Objects.nonNull(lastMsgId), Message::getId, lastMsgId);
        }, Message::getId);
    }

    /**
     * 乐观更新消息类型
     */
    public boolean riseOptimistic(Long id, Integer oldType, Integer newType) {
        return lambdaUpdate()
                .eq(Message::getId, id)
                .eq(Message::getType, oldType)
                .set(Message::getType, newType)
                .update();
    }

    public Integer getGapCount(Long roomId, Long fromId, Long toId) {
        return lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .gt(Message::getId, fromId)
                .le(Message::getId, toId)
                .count();
    }

    public void invalidByUid(Long uid) {
        lambdaUpdate()
                .eq(Message::getFromUid, uid)
                .set(Message::getStatus, MessageStatusEnum.DELETE.getStatus())
                .update();
    }

    public Integer getUnReadCount(Long roomId, Date readTime) {
        return lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .gt(Objects.nonNull(readTime), Message::getCreateTime, readTime)
                .count();
    }
}
