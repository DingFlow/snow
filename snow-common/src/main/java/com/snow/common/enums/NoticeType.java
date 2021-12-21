package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title: 系统通知类型
 * @Description:
 * @date 2021/6/11 14:10
 */
public enum NoticeType {
    NOTICE_TYPE("1", "系统公告"),
    NOTIFY_TYPE("2", "系统通知"),
    SYS_BANNER("2", "Banner")
    ;

    private final String code;
    private final String info;

    NoticeType(String code, String info)
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
