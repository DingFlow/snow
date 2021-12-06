package com.snow.flowable.common.enums;

/**
 * @author qimingjin
 * @Title: 流程类型
 * @Description:
 * @date 2021/12/6 15:18
 */
public enum FlowTypeEnum {

    API_PROCESS("API_PROCESS", "api流程"),

    FORM_PROCESS("FORM_PROCESS", "表单流程"),

  ;

    private final String code;
    private final String info;

    FlowTypeEnum(String code, String info)
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

    public static FlowTypeEnum getByCode(String code) {
        for (FlowTypeEnum value : FlowTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
