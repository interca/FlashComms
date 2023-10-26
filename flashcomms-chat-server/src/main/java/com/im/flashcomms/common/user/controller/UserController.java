package com.im.flashcomms.common.user.controller;


import com.im.flashcomms.common.common.domain.vo.resp.ApiResult;
import com.im.flashcomms.common.common.exception.BusinessErrorEnum;
import com.im.flashcomms.common.common.exception.BusinessException;
import com.im.flashcomms.common.common.exception.CommonErrorEnum;
import com.im.flashcomms.common.common.utils.RequestHolder;
import com.im.flashcomms.common.user.domain.dto.ItemInfoDTO;
import com.im.flashcomms.common.user.domain.dto.SummeryInfoDTO;
import com.im.flashcomms.common.user.domain.enums.RoleEnum;
import com.im.flashcomms.common.user.domain.vo.req.user.*;
import com.im.flashcomms.common.user.domain.vo.resp.user.BadgeResp;
import com.im.flashcomms.common.user.domain.vo.resp.user.UserInfoResp;
import com.im.flashcomms.common.user.service.IRoleService;
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


    @Autowired
    private IRoleService iRoleService;

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

    @PutMapping("/badge")
    @ApiOperation("佩戴勋章")
    public ApiResult<Void> wearingBadge(@Valid @RequestBody WearingBadgeReq req){
         userService.wearingBadge(RequestHolder.get().getUid(),req.getBadgeId());
         return ApiResult.success();
    }


    @PutMapping("/black")
    @ApiOperation("拉黑")
    public ApiResult<Void> wearingBadge(@Valid @RequestBody BlackReq req){
        Long uid = RequestHolder.get().getUid();
        boolean b = iRoleService.hasPower(uid, RoleEnum.ADMIN);
        if(!b){
            throw new  BusinessException(BusinessErrorEnum.BUSINESS_ERROR.getCode(),"没权限");
        }
        userService.black(req);
        return ApiResult.success();
    }



    @PostMapping("/public/summary/userInfo/batch")
    @ApiOperation("用户聚合信息-返回的代表需要刷新的")
    public ApiResult<List<SummeryInfoDTO>> getSummeryUserInfo(@Valid @RequestBody SummeryInfoReq req) {
        return ApiResult.success(userService.getSummeryUserInfo(req));
    }

    @PostMapping("/public/badges/batch")
    @ApiOperation("徽章聚合信息-返回的代表需要刷新的")
    public ApiResult<List<ItemInfoDTO>> getItemInfo(@Valid @RequestBody ItemInfoReq req) {
        return ApiResult.success(userService.getItemInfo(req));
    }
}

