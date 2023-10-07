package com.im.flashcomms.common.user.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-07
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/get")
    public  String test(){
        return "123";
    }
}

