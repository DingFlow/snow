package com.snow.common.enums;

/**
 * 操作状态
 * 
 * @author snow
 */
public enum DingTalkSyncType
{
    /**
     * 自动
     */
    AUTOMATIC(1),

    /**
     * 手动
     */
    MANUAL(2);

    private final Integer code;

    private DingTalkSyncType(Integer code)
    {
        this.code = code;
    }

    public Integer getCode()
    {
        return code;
    }
}
