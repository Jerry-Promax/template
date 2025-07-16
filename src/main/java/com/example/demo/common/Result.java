package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;
    public static final String CODE_SUCCESS = "200";
    public static final String CODE_AUTH_ERROR = "401";
    public static final String CODE_SYS_ERROR = "500";
    public static Result success(){
        return new Result(CODE_SUCCESS,"请求成功",null);
    }
    public static Result success(Object data){
        return new Result(CODE_SUCCESS,"请求成功",data);
    }
    public static Result error(String msg){
        return new Result(CODE_SYS_ERROR,msg,null);
    }
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }
}