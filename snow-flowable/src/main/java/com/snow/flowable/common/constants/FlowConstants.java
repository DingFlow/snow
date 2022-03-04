package com.snow.flowable.common.constants;

import java.awt.*;

/**
 * @author qimingjin
 * @Title: 流程定义常量类
 * @Description:
 * @date 2020/11/25 11:14
 */
public class FlowConstants {

    /**
     *流程通过变量名称
     */
    public static final String IS_PASS="isPass";

    /**
     * 流程发起变量名称
     */
    public static final String IS_START="isStart";

    /**
     *comment类型---意见
     */

    public static final String OPINION="OPINION";

    /**
     * 表单数据
     */
    public static final String APP_FORM="appForm";

    /**
     * 跳转数据详情页
     */
    public static final String BUS_VAR_URL="busVarUrl";


    /**
     * 流程开始用户标识
     */
    public static final String START_USER_ID="startUserId";

    /**
     * 表单数据
     */
    public static final String FORM_DATA="formData";

    /**
     * DingFlow表单id
     */
    public static final String DF_FORM_ID="dingFlowFormId";
    /**
     * 流程类型
     */
    public static final String PROCESS_TYPE="oaProcessType";
    /** 动态流程图颜色定义 **/
    public static final Color COLOR_NORMAL = new Color(0, 205, 0);
    public static final Color COLOR_CURRENT = new Color(255, 0, 0);

    /** 定义生成流程图时的边距(像素) **/
    public static final int PROCESS_PADDING = 5;

    /**
     * 最后一个操作人
     */
    public static final String LAST_OPERATOR="lastOperator";
}
