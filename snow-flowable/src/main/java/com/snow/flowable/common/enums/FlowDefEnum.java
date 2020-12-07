package com.snow.flowable.common.enums;

/**
 * @author qimingjin
 * @Title: 流程定义枚举
 * @Description:
 * @date 2020/12/7 15:54
 */
public enum  FlowDefEnum {
    SNOW_OA_LEAVE("snow_oa_leave", "请假申请流程"),

    NO_FINISHED("", "");

    private final String code;
    private final String info;

    FlowDefEnum(String code, String info)
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

    public static FlowDefEnum getByCode(String code) {
        for (FlowDefEnum value : FlowDefEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
