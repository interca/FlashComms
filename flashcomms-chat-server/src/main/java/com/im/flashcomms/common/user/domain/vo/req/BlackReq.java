package com.im.flashcomms.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BlackReq {

    @ApiModelProperty("用户uid")
    @NotBlank
    private Long uid;
}
