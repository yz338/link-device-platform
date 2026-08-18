package com.devicemanagement.common.exception;

/** 用于表达可预期的业务错误，由全局异常处理器统一返回。 */
public class BusinessException extends RuntimeException {

    /** 使用面向前端的业务提示创建异常。 */
    public BusinessException(String message) {
        super(message);
    }
}
