package com.snow.dingtalk.common;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/16 11:02
 */
public class BaseConstantUrl {

    /**
     * 创建用户
     */
    public static final String USER_CREATE= "https://oapi.dingtalk.com/topapi/v2/user/create";
    /**
     * 删除用户
     */
    public static final String USER_DELETE=  "https://oapi.dingtalk.com/topapi/v2/user/delete";

    /**
     * 编辑用户
     */
    public static final String USER_UPDATE= "https://oapi.dingtalk.com/topapi/v2/user/update";

    /**
     * 根据id获取用户信息
     */
    public static final String GET_USER_BY_ID=  "https://oapi.dingtalk.com/topapi/v2/user/get";

    /**
     * 根据部门获取用户信息
     */
    public static final String GET_USERINFO_BY_DEPT=  "https://oapi.dingtalk.com/topapi/v2/user/list";

    /**
     * 获取用户UNION_ID
     */
    public static final String GET_USER_UNION_ID= "https://oapi.dingtalk.com/topapi/user/getbyunionid";

    /**
     * 通过临时授权码获取授权用户的个人信息
     */
    public static final String GET_USER_BY_CODE="https://oapi.dingtalk.com/sns/getuserinfo_bycode";

    /**
     * 获取token url
     */
    public static final String GET_TOKEN_URL="https://oapi.dingtalk.com/gettoken";
    /**
     * 创建部门
     */
    public static final String DEPARTMENT_CREATE="https://oapi.dingtalk.com/department/create";
    /**
     * 更新部门
     */
    public static final String DEPARTMENT_UPDATE="https://oapi.dingtalk.com/topapi/v2/department/update";
    /**
     * 删除部门
     */
    public static final String DEPARTMENT_DELETE="https://oapi.dingtalk.com/topapi/v2/department/delete";
    /**
     * 根据ID获取部门信息
     */
    public static final String GET_DEPARTMENT_BY_ID="https://oapi.dingtalk.com/topapi/v2/department/get";

    /**
     * 获取钉钉部门信息
     */
    public static final String DEPARTMENT_LIST="https://oapi.dingtalk.com/department/list";

    /**
     * 创建流程
     *
     */
    public static final String FLOW_CREATE="https://oapi.dingtalk.com/topapi/processinstance/create";

    /**
     * 创建任务待办
     */
    public static final String WORK_RECORD_CREATE="https://oapi.dingtalk.com/topapi/workrecord/add";


    /**
     * 通过ID获取我的代办
     */
    public static final String GET_WORK_RECORD_USER_ID_="https://oapi.dingtalk.com/topapi/workrecord/getbyuserid";
    /**
     * 更新待办
     */
    public static final String WORK_RECORD_UPDATE="https://oapi.dingtalk.com/topapi/workrecord/update";

    /**
     * 注册回调
     */
    public static final String REGISTER_CALL_BACK="https://oapi.dingtalk.com/call_back/register_call_back";
    /**
     * 更新回调
     */
    public static final String UPDATE_CALL_BACK = "https://oapi.dingtalk.com/call_back/update_call_back";

    /**
     * 更新回调
     */
    public static final String DELETE_CALL_BACK = "https://oapi.dingtalk.com/call_back/delete_call_back";
    /**
     * 获取回调失败
     */
    public static final String CALL_BACK_FAILED_RESULT = "https://oapi.dingtalk.com/call_back/get_call_back_failed_result";


    /**
     * 发送工作通知
     */
    public static final String ASYNCSEND_V2="https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

    /**
     * 发送普通消息
     */
    public static final String SEND_TO_CONVERSATIO="https://oapi.dingtalk.com/message/send_to_conversation";


    /**
     * 创建公告
     */
    public static final String BLACKBOARD_CREATE="https://oapi.dingtalk.com/topapi/blackboard/create";

    /**
     * 更新公告
     */
    public static final String BLACKBOARD_UPDATE="https://oapi.dingtalk.com/topapi/blackboard/update";

    /**
     * 删除公告
     */
    public static final String BLACKBOARD_DELETE="https://oapi.dingtalk.com/topapi/blackboard/delete";


    /**********************************************************
     * 钉钉官方流程
     * *********************************************************/


    public static final String SAVE_PROCESS="https://oapi.dingtalk.com/topapi/process/save";
    /**
     * 获取当前企业所有可管理的模版
     */
    public static final String GET_PROCESS_TEMPLATE_MANAGE="https://oapi.dingtalk.com/topapi/process/template/manage/get";

    /**
     * 发起流程实例（钉钉官方）
     */
    public static final String START_PROCESS_INSTANCE="https://oapi.dingtalk.com/topapi/processinstance/create";

    /**
     * 发起流程审批实例（自有流程）
     */
    public static final String START_FAKE_PROCESS_INSTANCE="https://oapi.dingtalk.com/topapi/process/workrecord/create";

    /**
     * 执行流程
     */
    public static final String EXECUTE_PROCESSINSTANCE="https://oapi.dingtalk.com/topapi/process/instance/execute";

    /**
     * 获取审批实例详情
     */
    public static final String GET_PROCESSINSTANCE="https://oapi.dingtalk.com/topapi/processinstance/get";

    /**
     * 终止流程
     */
    public static final String TERMINATE_PROCESSINSTANCE="https://oapi.dingtalk.com/topapi/process/instance/terminate";

    /**
     * 分页获取指定用户可见的审批表单列表
     */
    public static final String GET_PROCESSLIST_USERID="https://oapi.dingtalk.com/topapi/process/listbyuserid";

    /**
     * 创建待办事项()
     */
    public static final String CREATE_PROCESS_TASK="https://oapi.dingtalk.com/topapi/process/workrecord/task/create";

    /**
     *
     */
    public static final String UPDATE_PROCESS_TASK="https://oapi.dingtalk.com/topapi/process/workrecord/task/update";

    public static final String UPDATE_PROCESS_WORKRECORD="https://oapi.dingtalk.com/topapi/process/workrecord/update";

    //************************************************外部联系人

    /**
     * 创建外部联系人
     */
    public static final String CREATE_EXT_CONTACT_USER="https://oapi.dingtalk.com/topapi/extcontact/create";
    /**
     * 删除外部联系人
     */
    public static final String DELETE_EXT_CONTACT_USER="https://oapi.dingtalk.com/topapi/extcontact/delete";
    /**
     * 更新外部联系人
     */
    public static final String UPDATE_EXT_CONTACT_USER="https://oapi.dingtalk.com/topapi/extcontact/update";
    /**
     * 获取外部联系人列表
     */
    public static final String EXT_CONTACT_USER_LIST="https://oapi.dingtalk.com/topapi/extcontact/list";
    /**
     * 获取外部联系人详情
     */
    public static final String GET_EXT_CONTACT_USER="https://oapi.dingtalk.com/topapi/extcontact/get";
    /**
     * 外部联系人列表
     */
    public static final String EXT_CONTACT_USER_LABEL="https://oapi.dingtalk.com/topapi/extcontact/listlabelgroups";

    //*********************************************************考勤相关
    /**
     * 上传考勤记录
     */
    public static final String ATTENDANCE_RECORD_UPLOAD="https://oapi.dingtalk.com/topapi/attendance/record/upload";

    /**
     * 获取考勤记录
     */
    public static final String ATTENDANCE_RECORD_LIST="https://oapi.dingtalk.com/attendance/list";

    /**
     * 获取考勤记录详情
     */
    public static final String ATTENDANCE_RECORD_RECORD="https://oapi.dingtalk.com/attendance/listRecord";


}
