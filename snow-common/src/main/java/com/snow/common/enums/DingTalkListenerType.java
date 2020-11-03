package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/18 10:18
 */
public enum  DingTalkListenerType {
    DEPARTMENT_CREATE(1, "部门创建"),
    DEPARTMENT_UPDATE(2, "部门更新"),
    DEPARTMENT_DELETED(3, "部门删除"),

    USER_CREATED(4,"用户创建"),

    CALL_BACK_REGISTER(10, "回调注册"),
    ;



    private final Integer code;
    private final String info;

    DingTalkListenerType(Integer code, String info)
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
