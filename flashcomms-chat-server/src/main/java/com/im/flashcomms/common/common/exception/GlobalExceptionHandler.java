package com.im.flashcomms.common.common.exception;

import com.im.flashcomms.common.common.domain.vo.resp.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<?> methodArgumentNotValidException(MethodArgumentNotValidException e){
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField())
                .append(x.getDefaultMessage()).append(","));
        String s = errorMsg.toString();
        return ApiResult.fail(CommonErrorEnum.PARAM_VALID.getCode(), s.substring(0,s.length() - 1));
    }


    @ExceptionHandler(value = BusinessException.class)
    public ApiResult<?> businessException(BusinessException e){
        log.info("业务异常:{}",e.getMessage());
        return ApiResult.fail(e.getErrorCode(),e.getErrorMsg());
    }

    @ExceptionHandler(value = Throwable.class)
    public ApiResult<?> throwable(Throwable e){
        log.error("系统异常:{}",e.getMessage());
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR.getCode(), CommonErrorEnum.SYSTEM_ERROR.getMsg());
    }


}
