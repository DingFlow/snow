package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/16 13:30
 */
public enum  SyncLogType {
    SYNC_DINGTALK(1),

    SYNC_SYS(2);
    private final Integer code;

    private SyncLogType(Integer code)
    {
        this.code = code;
    }

    public Integer getCode()
    {
        return code;
    }
}
