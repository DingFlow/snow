package com.snow.common.exception.user;

/**
 * 用户账号已被删除
 * 
 * @author snow
 */
public class UserTypeException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserTypeException()
    {
        super("user.type.error", null);
    }
}
