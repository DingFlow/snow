package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/18 10:18
 */
public enum  DingTalkListenerType {
    DEPARTMENT_CREATE(1, 2,"部门创建"),

    DEPARTMENT_UPDATE(2, 2,"部门更新"),

    DEPARTMENT_DELETED(3,2,"部门删除"),

    USER_CREATE(1,1,"用户创建"),

    USER_DELETE(3,1,"用户删除"),

    CALL_BACK_REGISTER(1,10, "回调注册"),

    CALL_BACK_UPDATE(2,10, "回调更新"),

    CALL_BACK_DELETE(3,10, "回调删除"),

    CALL_BACK_FAILED_RESULT(23,10, "获取回调失败结果"),
    ;


    /**
     * 一级code
     */
    private final Integer code;
    /**
     * 二级code
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String info;

    DingTalkListenerType(Integer code, Integer type,String info)
    {
        this.code = code;
        this.info = info;
        this.type=type;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

    public Integer getType() {
        return type;
    }

}
