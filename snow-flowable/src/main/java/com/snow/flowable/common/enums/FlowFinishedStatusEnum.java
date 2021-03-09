package com.snow.flowable.common.enums;

/**
 * 用户状态
 * 
 * @author snow
 */
public enum FlowFinishedStatusEnum
{
    FLOW_ING(0, "进行中"),
    FLOW_FINISHED(1, "已完结");

    private final Integer code;
    private final String info;

    FlowFinishedStatusEnum(Integer code, String info)
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
