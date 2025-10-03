package com.akbar.handler;

import com.akbar.constant.MessageConstant;
import com.akbar.exception.BaseException;
import com.akbar.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常，并返回对应错误信息
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> exceptionHandler(Exception e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }


    /**
     * 捕获SQL异常，并返回对应错误信息
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        String message = e.getMessage();
        log.error("SQL异常：{}", message);
        // 异常信息是这样的：Duplicate entry 'akbar' for key 'employee.idx_username'
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + MessageConstant.ALREADY_EXIST;
            return Result.error(msg);
        }

        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
