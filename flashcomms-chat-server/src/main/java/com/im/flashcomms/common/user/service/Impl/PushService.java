package com.im.flashcomms.common.user.service.Impl;


import com.im.flashcomms.common.common.config.RabbitmqConfig;
import com.im.flashcomms.common.common.domain.dto.PushMessageDTO;
import com.im.flashcomms.common.websocket.domain.vo.resp.WSBaseResp;
import com.im.flashcomms.transaction.service.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Author: hyj
 * Date: 2023-08-12
 */
@Service
public class PushService {
    @Autowired
    private MQProducer mqProducer;

    public void sendPushMsg(WSBaseResp<?> msg, List<Long> uidList) {
        mqProducer.sendMsg(RabbitmqConfig.FLASHCOMMS_MSG_EXCHANGE, new PushMessageDTO(uidList, msg));
    }

    public void sendPushMsg(WSBaseResp<?> msg, Long uid) {
        mqProducer.sendMsg(RabbitmqConfig.FLASHCOMMS_MSG_EXCHANGE, new PushMessageDTO(uid, msg));
    }

    public void sendPushMsg(WSBaseResp<?> msg) {
        mqProducer.sendMsg(RabbitmqConfig.FLASHCOMMS_MSG_EXCHANGE, new PushMessageDTO(msg));
    }
}
