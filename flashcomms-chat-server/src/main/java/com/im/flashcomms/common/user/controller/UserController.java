package com.im.flashcomms.common.user.controller;


import com.im.flashcomms.common.common.domain.vo.resp.ApiResult;
import com.im.flashcomms.common.common.interceptor.TokenInterceptor;
import com.im.flashcomms.common.common.utils.RequestHolder;
import com.im.flashcomms.common.user.domain.vo.resp.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @GetMapping("/userInfo")
    @ApiOperation(value = "获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo(HttpServletRequest request){
        ApiResult<UserInfoResp> apiResult = new ApiResult<>();
        apiResult.setErrCode(1);
        apiResult.setErrMsg("hyj");
        UserInfoResp userInfoResp = new UserInfoResp();
        userInfoResp.setId(RequestHolder.get().getUid());
        apiResult.setData(userInfoResp);
        return apiResult;
    }
}

