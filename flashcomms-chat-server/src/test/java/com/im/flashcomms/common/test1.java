package com.im.flashcomms.common;

import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class test1 {


    @Autowired
    private UserMapper userMapper;

    @Test
    void test(){
        User user = new User();
        user.setName("hyj");
        user.setOpenId("123");
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }
}
