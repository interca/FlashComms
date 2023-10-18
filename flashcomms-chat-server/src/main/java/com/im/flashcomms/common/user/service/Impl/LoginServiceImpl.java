package com.im.flashcomms.common.user.service.Impl;

import com.im.flashcomms.common.common.constant.RedisKey;
import com.im.flashcomms.common.common.utils.JwtUtils;
import com.im.flashcomms.common.user.service.LoginService;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 过期时间
     */
    public  static final  int TOKEN_EXPIRE_DAYS = 3;

    /**
     * token续期阈值
     */
    public  static  final  int TOKEN_RENEWAL_DAYS = 1;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean verify(String token) {
        return false;
    }




    /**
     * 刷新token有效期
     * @param token
     */
    @Override
    public void renewalTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = stringRedisTemplate.getExpire(userTokenKey,TimeUnit.DAYS);
        //key不存在
        if(expireDays == -2){
            return;
        }
        if(expireDays < TOKEN_RENEWAL_DAYS){
            stringRedisTemplate.expire(getUserTokenKey(uid),TOKEN_EXPIRE_DAYS,TimeUnit.DAYS);
        }
    }

    /**
     * 用户登录并且生成token
     * @param id
     * @return
     */
    @Override
    public String login(Long id) {
        String token = jwtUtils.createToken(id);
        stringRedisTemplate.opsForValue().set(RedisKey.getKey(RedisKey.USER_TOKEN_STRING,id),token,TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    /**
     * 获取uid
     * @param token
     * @return
     */
    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if(Objects.isNull(uid)){
            return null;
        }
        String oldToken = stringRedisTemplate.opsForValue().get(getUserTokenKey(uid));
        if(StringUtils.isBlank(oldToken)){
            return null;
        }
        return Objects.equals(token,oldToken)?uid:null;
    }


    private String getUserTokenKey(Long uid){
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING,uid);
    }
}
