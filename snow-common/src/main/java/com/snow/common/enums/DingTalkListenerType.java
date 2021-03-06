package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/18 10:18
 */
public enum  DingTalkListenerType {
    DEPARTMENT_CREATE(1, 2,"org_dept_create"),

    DEPARTMENT_UPDATE(2, 2,"org_dept_modify"),

    DEPARTMENT_DELETED(3,2,"org_dept_remove"),

    USER_CREATE(1,1,"user_add_org"),

    USER_DELETE(3,1,"user_leave_org"),

    CALL_BACK_REGISTER(1,10, "回调注册"),

    CALL_BACK_UPDATE(2,10, "回调更新"),

    CALL_BACK_DELETE(3,10, "回调删除"),

    CALL_BACK_FAILED_RESULT(23,10, "获取回调失败结果"),

    WORK_RECODE_CREATE(1,20, "创建待办"),

    GET_WORK_RECORD_USER(2,20,"获取用户待办"),

    WORK_RECORD_UPDATE(3,20,"更新待办"),

    ASYNCSEND_V2(10,20,"发送消息")
    ;


    /**WORK_RECODE_CREATE
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

    public static DingTalkListenerType getType(String info) {
        for (DingTalkListenerType  dingTalkListenerType:DingTalkListenerType.values()){
            if(dingTalkListenerType.getInfo().equals(info)){
                return dingTalkListenerType;
            }
        }
        return null;
    }

}
