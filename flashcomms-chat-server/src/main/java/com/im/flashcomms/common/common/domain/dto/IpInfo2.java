package com.im.flashcomms.common.common.domain.dto;

import com.im.flashcomms.common.user.domain.entity.IpDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IpInfo2 {

    private IpDetail data;

    private String msg;

    private String code;
}
