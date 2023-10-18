package com.im.flashcomms.common.user.controller;


import com.im.flashcomms.common.user.domain.vo.resp.UserInfoResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-07
 */
@RestController
@RequestMapping("/capi/user")
public class UserController {

    @GetMapping("/userInfo")
    public UserInfoResp getUserInfo(@RequestParam Long id){
        return null;
    }
}

