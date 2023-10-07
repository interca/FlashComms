package com.im.flashcomms.common;

import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

import java.util.List;

@SpringBootTest
public class test1 {


    @Autowired
    private UserMapper userMapper;

    @Test
    void test(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
}
