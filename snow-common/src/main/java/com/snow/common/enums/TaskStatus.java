package com.snow.common.enums;

/**
 * 任务状态
 * 
 * @author snow
 */
public enum TaskStatus
{
    UN_FINISH("un_finish", "未结束"), FINISH("finish", "结束");

    private final String code;
    private final String info;

    TaskStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
