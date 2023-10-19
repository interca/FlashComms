package com.im.flashcomms.common.user.controller;


import com.im.flashcomms.common.common.domain.vo.resp.ApiResult;
import com.im.flashcomms.common.common.utils.RequestHolder;
import com.im.flashcomms.common.user.domain.vo.req.ModifyNameReq;
import com.im.flashcomms.common.user.domain.vo.resp.BadgeResp;
import com.im.flashcomms.common.user.domain.vo.resp.UserInfoResp;
import com.im.flashcomms.common.user.domain.vo.req.WearingBadgeReq;
import com.im.flashcomms.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        userService.modifyName(RequestHolder.get().getUid(),modifyNameReq.getName());
        return ApiResult.success();
    }

    @GetMapping("/badges")
    @ApiOperation("可选徽章预览")
    public ApiResult<List<BadgeResp>> badges(){
        return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
    }

    @PutMapping("/bage")
    @ApiOperation("佩戴勋章")
    public ApiResult<Void> wearingBadge(@Valid @RequestBody WearingBadgeReq req){
         userService.wearingBadge(RequestHolder.get().getUid(),req.getItemId());
         return ApiResult.success();
    }
}

