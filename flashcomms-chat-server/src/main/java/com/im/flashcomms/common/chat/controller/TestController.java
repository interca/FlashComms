package com.im.flashcomms.common.chat.controller;

import com.im.flashcomms.common.chat.dao.MessageDao;
import com.im.flashcomms.common.chat.domain.entity.Message;
import com.im.flashcomms.common.common.config.RabbitmqConfig;
import com.im.flashcomms.common.common.domain.vo.resp.ApiResult;
import com.im.flashcomms.common.common.domain.vo.resp.IdRespVO;
import com.im.flashcomms.transaction.service.MQProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/capi/test/public")
@Api(tags = "测试接口")
@Slf4j
public class TestController {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MQProducer mqProducer;

    @PostMapping("secureInvoke")
    @ApiOperation("本地消息表")
    @Transactional
    public ApiResult<IdRespVO> secureInvoke(String msg){
        Message build = Message.builder()
                .fromUid(11006L)
                .type(1)
                .content(msg)
                .roomId(1L)
                .status(0)
                .build();
        messageDao.save(build);
        mqProducer.sendSecureMsg(RabbitmqConfig.FLASHCOMMS_MSG_EXCHANGE,msg,"flashcomms");
        return ApiResult.success();
    }
}
