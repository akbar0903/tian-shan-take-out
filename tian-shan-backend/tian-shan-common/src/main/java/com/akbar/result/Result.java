package com.akbar.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;  // 1成功，0和其它数字为失败
    private String msg;    // 错误信息
    private T data;        //数据

    public static final Integer SUCCESS_CODE = 1;
    public static final Integer ERROR_CODE = 0;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = SUCCESS_CODE;
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = SUCCESS_CODE;
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = ERROR_CODE;
        result.msg = msg;
        return result;
    }
}
