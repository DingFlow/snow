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
    DEPARTMENT_DELETED(3, "部门删除");

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
