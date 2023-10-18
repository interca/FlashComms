package com.im.flashcomms.common.user.domain.vo.resp;

import lombok.Data;

/**
 * 返回用户对象
 */
@Data
public class UserInfoResp {
    private Long id;
    private String name;
    private String avatar;
    private Integer sex;
    /**
     * 改名卡次数
     */
    private Integer modifyName;
}
