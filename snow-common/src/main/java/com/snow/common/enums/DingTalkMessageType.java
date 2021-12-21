package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title: 交易类型枚举
 * @Description:
 * @date 2021/2/26 11:25
 */
public enum DingTalkMessageType {
    TEXT(1, "text"),
    LINK(2, "link"),
    MARKDOWN(3,"markdown");

    private final Integer code;
    private final String info;

    DingTalkMessageType(Integer code, String info)
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
