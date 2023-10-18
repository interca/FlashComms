package com.im.flashcomms.common.common.exception;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Charsets;
import com.im.flashcomms.common.common.domain.vo.resp.ApiResult;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 返回错误信息
 */
@AllArgsConstructor
public enum HttpErrorEnum {
    ACCESS_DENIED(401,"登陆失效请重新登陆");

    private Integer httpCode;

    private String desc;

    public  void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        response.getWriter().write(JSONUtil.toJsonStr(ApiResult.fail(httpCode,desc)));
    }



}
