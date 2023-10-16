package com.im.flashcomms.common.user.service.Impl;

import com.im.flashcomms.common.common.utils.JwtUtils;
import com.im.flashcomms.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {


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
     * 用户登录
     * @param id
     * @return
     */
    @Override
    public String login(Long id) {
        return jwtUtils.createToken(id);
    }

    @Override
    public Long getValidUid(String token) {
        return null;
    }
}
