package com.im.flashcomms.common.user.domain.vo.req.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BlackReq {

    @ApiModelProperty("用户uid")
    private Long uid;
}
