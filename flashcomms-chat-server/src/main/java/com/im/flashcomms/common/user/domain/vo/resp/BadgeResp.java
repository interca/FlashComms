package com.im.flashcomms.common.user.domain.vo.resp;


/**
 * 返回的用户背包对象
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BadgeResp {

    @ApiModelProperty(value = "徽章id")
    private Long id;

    @ApiModelProperty(value = "徽章图标")
    private String avatar;

    @ApiModelProperty(value = "徽章描述")
    private Integer describe;

    @ApiModelProperty(value = "是否拥有，0-否，1-是")
    private Integer obtain;

    @ApiModelProperty(value =  "是否佩戴,0-否，1-是")
    private Integer wearing;
}
