package com.snow.flowable.common.enums;

/**
 * @author qimingjin
 * @Title: 流程定义枚举
 * @Description:
 * @date 2020/12/7 15:54
 */
public enum FlowEventTypeEnum {

    PROCESS_STARTED("PROCESS_STARTED", "流程开始"),

    PROCESS_COMPLETED("PROCESS_COMPLETED", "流程完结"),

    TASK_CREATED("TASK_CREATED", "任务创建"),

    TASK_OWNER_CHANGED("TASK_OWNER_CHANGED","转办任务"),

    TASK_COMPLETED("TASK_COMPLETED","完成任务");

    private final String code;
    private final String info;

    FlowEventTypeEnum(String code, String info)
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

    public static FlowEventTypeEnum getByCode(String code) {
        for (FlowEventTypeEnum value : FlowEventTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
