package com.im.flashcomms.common.chat.service;

import com.im.flashcomms.common.chat.domain.vo.request.*;
import com.im.flashcomms.common.chat.domain.vo.response.ChatMemberListResp;
import com.im.flashcomms.common.chat.domain.vo.response.ChatRoomResp;
import com.im.flashcomms.common.chat.domain.vo.response.MemberResp;
import com.im.flashcomms.common.common.domain.vo.req.CursorPageBaseReq;
import com.im.flashcomms.common.common.domain.vo.resp.CursorPageBaseResp;
import com.im.flashcomms.common.websocket.domain.vo.resp.ChatMemberResp;


import java.util.List;

/**
 * Description:
 * Author: <a href="https://github.com/zongzibinbin">im</a>
 * Date: 2023-07-22
 */
public interface RoomAppService {
    /**
     * 获取会话列表--支持未登录态
     */
    CursorPageBaseResp<ChatRoomResp> getContactPage(CursorPageBaseReq request, Long uid);

    /**
     * 获取群组信息
     */
    MemberResp getGroupDetail(Long uid, long roomId);

    CursorPageBaseResp<ChatMemberResp> getMemberPage(MemberReq request);

    List<ChatMemberListResp> getMemberList(ChatMessageMemberReq request);

    void delMember(Long uid, MemberDelReq request);

    void addMember(Long uid, MemberAddReq request);

    Long addGroup(Long uid, GroupAddReq request);

    ChatRoomResp getContactDetail(Long uid, Long roomId);

    ChatRoomResp getContactDetailByFriend(Long uid, Long friendUid);
}
