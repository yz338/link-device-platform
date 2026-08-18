package com.devicemanagement.common.exception;

import com.devicemanagement.common.Result;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 将 Controller 与 Service 抛出的异常统一转换为 JSON 响应。 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 处理 Service 主动抛出的可预期业务异常。 */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBusinessException(BusinessException exception) {
        return Result.error(400, exception.getMessage());
    }

    /** 处理请求 DTO 的字段校验异常，只返回第一条错误。 */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(Exception exception) {
        String message = "请求参数不正确";
        if (exception instanceof MethodArgumentNotValidException validException) {
            message = validException.getBindingResult().getAllErrors().stream()
                    .findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).orElse(message);
        }
        if (exception instanceof BindException bindException) {
            message = bindException.getAllErrors().stream()
                    .findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).orElse(message);
        }
        return Result.error(400, message);
    }

    /** 处理请求参数绑定时产生的约束校验异常。 */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolation(ConstraintViolationException exception) {
        return Result.error(400, exception.getMessage());
    }

    /** 处理未预期异常，并将完整堆栈仅记录在服务端日志。 */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception exception) {
        log.error("系统异常", exception);
        return Result.error(500, "系统异常，请稍后重试");
    }
}
