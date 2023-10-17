package com.im.flashcomms.common;

import com.im.flashcomms.common.common.utils.JwtUtils;
import com.im.flashcomms.common.user.mapper.UserMapper;
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


    //@Test
    void test() throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxService.getQrcodeService().qrCodeCreateTmpTicket(1, 1000);
        System.out.println(wxMpQrCodeTicket.getUrl());
    }

    @Test
    void  test2(){
        RLock lock = redissonClient.getLock("123");
        boolean b = lock.tryLock();
        System.out.println(b);
        lock.unlock();
    }
}
