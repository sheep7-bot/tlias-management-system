package com.itheima.exception;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理 Controller 层抛出的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理数据库唯一键冲突异常
     *
     * @param e 异常对象
     * @return 统一响应结果
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("数据库唯一键冲突: {}", e.getMessage());

        String errorMessage = e.getMessage();
        if (errorMessage != null) {
            if (errorMessage.contains("emp.phone")) {
                return Result.error("手机号已存在，请使用其他手机号");
            } else if (errorMessage.contains("emp.username")) {
                return Result.error("用户名已存在，请使用其他用户名");
            }
        }

        return Result.error("数据已存在，请检查后重试");
    }

    /**
     * 处理业务异常（RuntimeException）
     *
     * @param e 异常对象
     * @return 统一响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 处理系统异常（其他所有异常）
     *
     * @param e 异常对象
     * @return 统一响应结果
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统繁忙，请稍后重试");
    }
}
