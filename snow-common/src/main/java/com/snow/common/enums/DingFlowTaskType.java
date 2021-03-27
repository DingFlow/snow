package com.snow.common.enums;

/**
 * @program: snow
 * @description 钉钉任务类型
 * @author: 没用的阿吉
 * @create: 2021-03-24 16:17
 **/
public enum DingFlowTaskType {

    NEW("NEW", "未启动"),
    RUNNING("RUNNING", "待处理"),
    PAUSED("PAUSED", "暂停"),
    CANCELED("CANCELED", "取消"),
    COMPLETED("COMPLETED", "完成"),
    TERMINATED("TERMINATED", "终止");

    private final String code;
    private final String info;

    DingFlowTaskType(String code, String info)
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

    public static DingFlowTaskType getType(String info) {
        for (DingFlowTaskType dingFLowOperationType:DingFlowTaskType.values()){
            if(dingFLowOperationType.getInfo().equals(info)){
                return dingFLowOperationType;
            }
        }
        return null;
    }

    public static DingFlowTaskType getCode(String code) {
        for (DingFlowTaskType dingFLowOperationType:DingFlowTaskType.values()){
            if(dingFLowOperationType.getCode().equals(code)){
                return dingFLowOperationType;
            }
        }
        return null;
    }
}
