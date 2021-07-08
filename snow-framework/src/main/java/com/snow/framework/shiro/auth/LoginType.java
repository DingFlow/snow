package com.snow.framework.shiro.auth;

/**
 * 登录类型枚举类
 * 
 * @author ruoyi
 */
public enum LoginType
{
    /**
     * 密码登录
     */
    PASSWORD("password"),
    /**
     * 免密码登录
     */
    NOPASSWD("nopasswd"),

    /**
     * 官方网站
     */
    OFFICIAL_WEBSITE("official_website");

    private String desc;

    LoginType(String desc)
    {
        this.desc = desc;
    }

    public String getDesc()
    {
        return desc;
    }
}