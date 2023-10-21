package com.im.flashcomms.common.user.domain.entity;

import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpInfo {
    //注册时的ip
    private String createIp;
    //注册时的ip详情
    private IpDetail createIpDetail;
    //最新登录的ip
    private String updateIp;
    //最新登录的ip详情
    private IpDetail updateIpDetail;

    /**
     * 刷新ip
     * @param ip
     */
    public void refreshIp(String ip) {
        if(StringUtils.isBlank(ip)){
            return;
        }
        //空的才更新
        if(StringUtils.isBlank(createIp)){
            createIp = ip;
        }
        updateIp = ip;
    }

    /**
     * 判断是否需要刷新
     */
    public String needRefreshIp() {
        boolean present = Optional.ofNullable(updateIpDetail)
                .map(IpDetail::getIp)
                .filter(ip -> Objects.equals(ip, updateIp))
                .isPresent();
        return present?null:updateIp;
    }


    /**
     * ip详细刷新
     * @param ipDetail
     */
    public void refreshIpDetail(IpDetail ipDetail) {
        if(Objects.equals(createIp,ipDetail.getIp())){
            createIpDetail = ipDetail;
        }
        if(Objects.equals(updateIp,ipDetail.getIp())){
            updateIpDetail = ipDetail;
        }

    }
}
