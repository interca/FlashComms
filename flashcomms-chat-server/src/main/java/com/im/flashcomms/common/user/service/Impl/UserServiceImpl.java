package com.im.flashcomms.common.user.service.Impl;

import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    @Transactional
    public Long  register(User insert) {
        boolean save = userDao.save(insert);
        //用户注册事件 todo
        return insert.getId();
    }
}
