package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/21 15:21
 */
public enum  WorkRecordStatus {
    FINISHED(1, "完成"), NO_FINISHED(0, "未完成");

    private final Integer code;
    private final String info;

    WorkRecordStatus(Integer code, String info)
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
