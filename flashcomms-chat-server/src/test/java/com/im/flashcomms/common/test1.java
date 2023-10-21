package com.im.flashcomms.common;

import com.im.flashcomms.common.common.Thread.MyUncaughtExceptionHandler;
import com.im.flashcomms.common.common.config.ThreadPoolConfig;
import com.im.flashcomms.common.common.utils.JwtUtils;
import com.im.flashcomms.common.user.domain.enums.IdempotentEnum;
import com.im.flashcomms.common.user.domain.enums.ItemEnum;
import com.im.flashcomms.common.user.mapper.UserMapper;
import com.im.flashcomms.common.user.service.IUserBackpackService;
import com.im.flashcomms.common.user.service.LoginService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@SpringBootTest
public class test1 {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
     WxMpService wxService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private LoginService loginService;

    @Autowired
    private IUserBackpackService iUserBackpackService;

    @Autowired
    @Qualifier(ThreadPoolConfig.FLASHCOMMS_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    //@Test
    void test() throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxService.getQrcodeService().qrCodeCreateTmpTicket(1, 1000);
        System.out.println(wxMpQrCodeTicket.getUrl());
    }

    @Test
    void  test2(){
       String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjExMDAxLCJjcmVhdGVUaW1lIjoxNjk3NjA2NjM3fQ.td41kLB1z3_tbpVQ4ScDJVnX45tNWL1wCkTfhStLQ5E";
        Long validUid = loginService.getValidUid(token);
        System.out.println(validUid);
    }

    @Test
    void test3() throws InterruptedException {
        String token = loginService.login(11006L);
        System.out.println(token);
    }

    @Test
    void test4(){
      iUserBackpackService.acquireItem(11001l, ItemEnum.MODIFY_NAME_CARD.getId(),IdempotentEnum.UID,
              11006L + "");

    }
}
