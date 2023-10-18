package com.im.flashcomms.common.common.interceptor;

import com.im.flashcomms.common.common.exception.HttpErrorEnum;
import com.im.flashcomms.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

/**
 * token拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final  String HEADER_AUTHORIZATION = "Authorization";

    public static  final String AUTHORIZATION_SCHEMA = "Bearer ";


    public static  final String UID = "uid";

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        if(Objects.nonNull(validUid)){
           request.setAttribute(UID,validUid);
        }else {//用户未登录
            //判断是是不是public接口
            boolean a = isPublic(request);
            if(!a){
                //401
                HttpErrorEnum accessDenied = HttpErrorEnum.ACCESS_DENIED;
                accessDenied.sendHttpError(response);
                return false;
            }
        }
        return true;
    }

    private boolean isPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        boolean a = split.length > 2 && "public".equals(split[3]);
        return a;
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        //是不是以bearer开头
        String s = Optional.ofNullable(authorization)
                .filter(h -> h.startsWith(AUTHORIZATION_SCHEMA))
                .map(h -> h.replaceFirst(AUTHORIZATION_SCHEMA, ""))
                .orElse(null);

        return s;
    }
}
