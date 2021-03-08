package com.snow.flowable.common.enums;

/**
 * @program: snow
 * @description  流程实例状态，挂起或者激活
 * @author: 没用的阿吉
 * @create: 2020-12-13 19:54
 **/
public enum FlowInstanceEnum {


    ACTIVATE(1, "激活"),

    SUSPEND(2, "挂起");

    private final Integer code;
    private final String info;

    FlowInstanceEnum(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}