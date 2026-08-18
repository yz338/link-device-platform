package com.devicemanagement.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前后端统一响应对象。
 * code 为 200 表示业务成功，前端 Axios 只需按这一条约定处理。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /** 业务状态码，200 表示成功。 */
    private int code;

    /** 面向前端的提示信息。 */
    private String message;

    /** 业务数据，可为空。 */
    private T data;

    /** 创建成功响应。 */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /** 创建指定业务状态码的失败响应。 */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
