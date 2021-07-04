package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/21 15:21
 */
public enum MessageReadStatus {
    NO_READ(0L, "未读"), READ(1L, "已读");

    private final Long code;
    private final String info;

    MessageReadStatus(Long code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Long getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
