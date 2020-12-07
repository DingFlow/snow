package com.snow.common.enums;

/**
 * 业务流程状态
 * 
 * @author snow
 */
public enum ProcessStatus
{
    /**
     * 待发起
     */
    WAITING_TO_SEND ,

    /**
     * 审批中
     */
    CHECKING,
    /**
     * 审批通过
     */
    PASS,
    /**
     * 审批驳回
     */
    REJECT,
    /**
     * 取消
     */
    CANCEL
}
