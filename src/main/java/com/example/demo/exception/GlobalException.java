package com.example.demo.exception;

import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */
@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result serviceException(ServiceException e){
        return Result.error(e.getCode(),e.getMessage());
    }

    /**
     * 全局的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result globalException(Exception e){
        e.printStackTrace();
        return Result.error("500","系统错误");
    }
}