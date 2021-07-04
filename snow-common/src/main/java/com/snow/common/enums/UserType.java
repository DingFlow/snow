package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/6/11 14:10
 */
public enum UserType {
    SYS_USER_TYPE("00", "系统用户"), FRONT_USER_TYPE("01", "前台用户");

    private final String code;
    private final String info;

    UserType(String code, String info)
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
