package com.im.flashcomms.common;

import com.im.flashcomms.common.common.utils.JwtUtils;
import com.im.flashcomms.common.user.mapper.UserMapper;
import com.im.flashcomms.common.user.service.LoginService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;


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


    //@Test
    void test() throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxService.getQrcodeService().qrCodeCreateTmpTicket(1, 1000);
        System.out.println(wxMpQrCodeTicket.getUrl());
    }

    @Test
    void  test2(){
       String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjExMDAxLCJjcmVhdGVUaW1lIjoxNjk3NTUwNjMyfQ.xDyN0nsRWed0ghxLLkWbZS27uXaE6vwURPxk6DxMuZ8";
        Long validUid = loginService.getValidUid(token);
        System.out.println(validUid);
    }
}
