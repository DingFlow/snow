package com.snow.framework.shiro.auth;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 自定义登录Token
 * 
 * @author ruoyi
 */
public class UserToken extends UsernamePasswordToken
{
    private static final long serialVersionUID = 1L;

    private LoginType type;

    public UserToken()
    {
    }

    public UserToken(String username, String password, LoginType type, boolean rememberMe)
    {
        super(username, password, rememberMe);
        this.type = type;
    }

    public UserToken(String username, LoginType type)
    {
        super(username, "", false, null);
        this.type = type;
    }

    public UserToken(String username, String password, LoginType type)
    {
        super(username, password, false, null);
        this.type = type;
    }

    public LoginType getType()
    {
        return type;
    }

    public void setType(LoginType type)
    {
        this.type = type;
    }
}