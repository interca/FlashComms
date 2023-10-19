package com.im.flashcomms.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 接受用户改名信息实体类
 */
@Data
public class ModifyNameReq {

    @ApiModelProperty("用户名")
    @NotBlank
    @Length(max = 6,message = "用户名不能太长")
    private String name;
}
