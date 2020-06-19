package com.mp.common.exception;

/**
 * Author: Xiaoer
 * Date: 2020-06-18
 */
public enum GCommonExceptionEnum implements GExceptionEnum {
    SYS_ERROR(10000, "系统错误"),
    PARAM_ERROR(10001, "参数校验错误");

    private int code;
    private String msg;

    GCommonExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
