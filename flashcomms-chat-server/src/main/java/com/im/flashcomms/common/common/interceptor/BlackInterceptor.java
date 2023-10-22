package com.im.flashcomms.common.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.im.flashcomms.common.common.domain.dto.RequestInfo;
import com.im.flashcomms.common.common.exception.BusinessException;
import com.im.flashcomms.common.common.exception.CommonErrorEnum;
import com.im.flashcomms.common.common.exception.HttpErrorEnum;
import com.im.flashcomms.common.common.utils.RequestHolder;
import com.im.flashcomms.common.user.cache.UserCache;
import com.im.flashcomms.common.user.domain.enums.BlackTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 黑名单拦截器
 */
@Component
public class BlackInterceptor implements HandlerInterceptor {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Integer, Set<String>> blackMap = userCache.getBlackMap();
        RequestInfo requestInfo = RequestHolder.get();
        Boolean inBlackList = inBlackList(requestInfo.getUid(), blackMap.get(BlackTypeEnum.UID.getType()));
        Boolean inBlackList1 = inBlackList(requestInfo.getIp(), blackMap.get(BlackTypeEnum.IP.getType()));
        if(!inBlackList1 || !inBlackList){
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        return true;
    }

    private Boolean inBlackList(Object target, Set<String> strings) {
        if(Objects.isNull(target) || CollectionUtil.isEmpty(strings)){
            return false;
        }
        return strings.contains(target.toString());
    }


}
