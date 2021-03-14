package com.snow.common.enums;

/**
 * email搜索类型
 */
public enum SysEmailSearchType {

    COMMON(0, "common"),
    INBOX(6, "inbox"),
    SEND_MAIL(7, "send_mail"),
    IMPORTANT(3, "important"),
    DRAFTS(1, "drafts"),
    TRASH(4, "trash");

    private final Integer code;
    private final String info;

    SysEmailSearchType(Integer code, String info)
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
