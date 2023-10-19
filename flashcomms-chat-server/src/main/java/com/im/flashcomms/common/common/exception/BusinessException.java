package com.im.flashcomms.common.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 业务异常
 */
@Data
@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    protected  Integer errorCode;

    protected String errorMsg;
}
