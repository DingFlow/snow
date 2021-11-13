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

    //*****************************用户相关

    USER_CREATE(1,1,"user_add_org"),

    USER_DELETE(3,1,"user_leave_org"),

    USER_UPDATE(2,1,"user_modify_org"),


    //*********************外部联系人
    CREATE_EXT_CONTACT_USER(1,3,"create_ext_contact_user"),

    UPDATE_EXT_CONTACT_USER(2,3,"update_ext_contact_user"),

    DELETE_EXT_CONTACT_USER(3,3,"delete_ext_contact_user"),

    EXT_CONTACT_USER_LIST(4,3,"ext_contact_user_list"),

    GET_EXT_CONTACT_USER(5,3,"get_ext_contact_user"),

    //*******************回调
    CALL_BACK_CHECK_URL(1,10, "回调check_url"),

    CALL_BACK_REGISTER(1,10, "回调注册"),

    CALL_BACK_UPDATE(2,10, "回调更新"),

    CALL_BACK_DELETE(3,10, "回调删除"),

    CALL_BACK_FAILED_RESULT(23,10, "获取回调失败结果"),


    //*****************待办
    WORK_RECODE_CREATE(1,20, "创建待办"),

    GET_WORK_RECORD_USER(2,20,"获取用户待办"),

    WORK_RECORD_UPDATE(3,20,"更新待办"),

    WORK_RECORD_DELETE(4,20,"删除待办"),


    UPDATE_TODO_TASK_EXECUTOR_STATUS(7,20,"更新任务执行状态"),

    WORK_RECODE_OLD_CREATE(8,20, "创建待办(old)"),
    /**
     * 审批任务开始、结束、转交。
     */
    BPMS_TASK_CHANGE(5,20,"bpms_task_change"),

    /**
     *
     * 审批实例开始、结束。
     */
    BPMS_INSTANCE_CHANGE(6,20,"bpms_instance_change"),

    /**
     * 发送钉钉工作通知
     */
    ASYNCSEND_V2(10,20,"发送钉钉消息"),


    BLACKBOARD_CREATE(1,30,"公告创建"),

    BLACKBOARD_UPDATE(2,30,"公告更新"),

    BLACKBOARD_DELETE(3,30,"公告删除"),

    GET_TOKEN(1,40,"获取DingTalkToken"),

    GET_TOKEN_V2(2,40,"获取DingTalkTokenV2")
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
