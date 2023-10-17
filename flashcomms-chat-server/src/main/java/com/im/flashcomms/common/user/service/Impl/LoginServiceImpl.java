package com.im.flashcomms.common.user.service.Impl;

import com.im.flashcomms.common.common.constant.RedisKey;
import com.im.flashcomms.common.common.utils.JwtUtils;
import com.im.flashcomms.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean verify(String token) {
        return false;
    }

    @Override
    public void renewalTokenIfNecessary(String token) {

    }

    /**
     * 用户登录并且生成token
     * @param id
     * @return
     */
    @Override
    public String login(Long id) {
        String token = jwtUtils.createToken(id);
        stringRedisTemplate.opsForValue().set(RedisKey.getKey(RedisKey.USER_TOKEN_STRING,id),token,3, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        return null;
    }
}
