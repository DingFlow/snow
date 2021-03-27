package com.snow.common.enums;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-24 16:17
 **/
public enum DingFlowOperationType {

    EXECUTE_TASK_NORMAL("正常执行任务", "EXECUTE_TASK_NORMAL"),
    EXECUTE_TASK_AGENT("代理人执行任务", "EXECUTE_TASK_AGENT"),
    APPEND_TASK_BEFORE("前加签任务", "APPEND_TASK_BEFORE"),
    APPEND_TASK_AFTER("后加签任务", "APPEND_TASK_AFTER"),
    REDIRECT_TASK("转交任务", "REDIRECT_TASK"),
    START_PROCESS_INSTANCE("发起流程实例", "START_PROCESS_INSTANCE"),

    TERMINATE_PROCESS_INSTANCE("终止(撤销)流程实例", "TERMINATE_PROCESS_INSTANCE"),
    FINISH_PROCESS_INSTANCE("结束流程实例", "FINISH_PROCESS_INSTANCE"),
    ADD_REMARK("添加评论", "ADD_REMARK"),
    REDIRECT_PROCESS("审批退回", "redirect_process");

    private final String code;
    private final String info;

    DingFlowOperationType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

    public static DingFlowOperationType getType(String info) {
        for (DingFlowOperationType dingFLowOperationType:DingFlowOperationType.values()){
            if(dingFLowOperationType.getInfo().equals(info)){
                return dingFLowOperationType;
            }
        }
        return null;
    }
}
