package com.example.demo.common;


/**
 * 系统日志的操作类型枚举
 */
public enum LogType {

    ADD("新增"), UPDATE("修改"), DELETE( "删除"), LOGIN("登录"), REGISTER( "注册"),RESET("重置"),
    ASSIGN("分配权限"),AUDIT("审核"),BLACK("黑名单"), EXPORT("导出文件");

    private String value;

    public String getValue() {
        return value;
    }

    LogType(String value) {
        this.value = value;
    }
}