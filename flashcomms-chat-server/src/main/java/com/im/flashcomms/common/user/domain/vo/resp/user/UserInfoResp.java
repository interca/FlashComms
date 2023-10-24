package com.im.flashcomms.common.user.domain.vo.resp.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 返回用户对象
 */
@Data
public class UserInfoResp {
    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户性别")
    private Integer sex;

    @ApiModelProperty(value = "用户改名卡次数")
    private Integer modifyName;
}
