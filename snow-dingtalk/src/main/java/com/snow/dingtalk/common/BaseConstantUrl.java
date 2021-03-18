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
     * 发送普通消息
     */
    public static final String ASYNCSEND_V2="https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";


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

    /**
     * 获取当前企业所有可管理的模版
     */
    public static final String GET_PROCESS_TEMPLATE_MANAGE="https://oapi.dingtalk.com/topapi/process/template/manage/get";

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
}
