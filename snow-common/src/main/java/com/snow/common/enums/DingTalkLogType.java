package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title: 钉钉日志类型
 * @Description:
 * @date 2020/9/18 10:18
 */
public enum DingTalkLogType {


    DEPARTMENT_CREATE(1, 1,"新建部门"),

    DEPARTMENT_UPDATE(1, 2,"更新部门"),

    DEPARTMENT_DELETED(1,3,"删除部门"),

    DEPARTMENT_QUERY(1,4,"查询"),

    //*****************************用户相关

    USER_CREATE(2,1,"新增用户"),

    USER_DELETE(2,3,"删除(离职)用户"),

    USER_UPDATE(2,2,"更新用户"),


    //*********************外部联系人
    CREATE_EXT_CONTACT_USER(3,1,"新增外部联系人"),

    UPDATE_EXT_CONTACT_USER(3,2,"更新外部联系人"),

    DELETE_EXT_CONTACT_USER(3,3,"删除外部联系人"),

    EXT_CONTACT_USER_LIST(3,4,"获取外部联系人列表"),

    GET_EXT_CONTACT_USER(3,4,"获取外部联系人详情"),

    //*******************回调
    CALL_BACK_CHECK_URL(4,6, "回调check_url"),

    CALL_BACK_REGISTER(4,1, "回调注册"),

    CALL_BACK_UPDATE(4,2, "回调更新"),

    CALL_BACK_DELETE(4,3, "回调删除"),

    CALL_BACK_FAILED_RESULT(4,4, "获取回调失败结果"),


    //*****************待办
    WORK_RECODE_CREATE(5,1, "创建待办"),

    GET_WORK_RECORD_USER(5,4,"获取用户待办"),

    WORK_RECORD_UPDATE(5,2,"更新待办"),

    WORK_RECORD_DELETE(5,3,"删除待办"),

    UPDATE_TODO_TASK_EXECUTOR_STATUS(5,2,"更新任务执行状态"),



    /**
     * 发送钉钉工作通知
     */
    ASYNCSEND_V2(6,5,"发送钉钉消息"),


    /**
     * 公告
     */
    BLACKBOARD_CREATE(7,1,"公告创建"),

    BLACKBOARD_UPDATE(7,2,"公告更新"),

    BLACKBOARD_DELETE(7,3,"公告删除"),

    /**
     * token
     */
    GET_TOKEN(8,4,"获取DingTalkToken"),

    GET_TOKEN_V2(8,4,"获取DingTalkTokenV2"),

    /**
     * 考勤
     */
    UPLOAD_ATTENDANCE_RECORD(9,1,"上传考勤"),

    ATTENDANCE_RECORD_LIST(9,2,"考勤记录"),

    ATTENDANCE_RECORD_RECORD(9,3,"考勤详情")
    ;


   //模块
    private final Integer moduleType;
    /**
     * 操作类型 1--新增 2--更新 3--删除 4- 查询 5--发送消息
     */
    private final Integer businessType;
    /**
     * 标题
     */
    private final String title;

    DingTalkLogType(Integer moduleType, Integer businessType, String title)
    {
        this.moduleType = moduleType;
        this.businessType = businessType;
        this.title=title;
    }

    public Integer getModuleType()
    {
        return moduleType;
    }

    public String getTitle()
    {
        return title;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public static DingTalkLogType getType(String title) {
        for (DingTalkLogType dingTalkListenerType: DingTalkLogType.values()){
            if(dingTalkListenerType.getTitle().equals(title)){
                return dingTalkListenerType;
            }
        }
        return null;
    }

}
