package com.im.flashcomms.common.user.controller;


import com.im.flashcomms.common.common.domain.vo.resp.ApiResult;
import com.im.flashcomms.common.common.interceptor.TokenInterceptor;
import com.im.flashcomms.common.common.utils.RequestHolder;
import com.im.flashcomms.common.user.domain.vo.req.ModifyNameReq;
import com.im.flashcomms.common.user.domain.vo.resp.UserInfoResp;
import com.im.flashcomms.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/userInfo")
    @ApiOperation(value = "获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo(){
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }


    @PutMapping("/name")
    @ApiOperation(value = "用户改名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq modifyNameReq){
        int a = 1 / 0;
        userService.modifyName(RequestHolder.get().getUid(),modifyNameReq.getName());
        return ApiResult.success();
    }
}

