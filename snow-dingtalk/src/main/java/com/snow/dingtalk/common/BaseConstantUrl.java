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
    public static final String USER_CREATE="https://oapi.dingtalk.com/user/create";
    /**
     * 获取token url
     */
    public static final String GET_TOKEN_URL="https://oapi.dingtalk.com/gettoken";
    /**
     * 创建部门
     */
    public static final String DEPARTMENT_CREATE="https://oapi.dingtalk.com/department/create";

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
}
