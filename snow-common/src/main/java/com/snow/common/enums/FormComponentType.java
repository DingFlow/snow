package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title: 表单组件类型
 * @Description:
 * @date 2020/9/18 10:18
 */
public enum FormComponentType {

    TEXT_FIELD(1, "inputDom","TextField"),

    TEXTAREA_FIELD(2, "textareaDom","TextareaField"),


    DDDATE_FIELD(3, "dateDom","DDDateField"),

    ;


    /**WORK_RECODE_CREATE
     * 一级code
     */
    private final Integer code;
    /**
     * 系统类型
     */
    private final String sysType;
    /**
     * 钉钉类型
     */
    private final String dingType;

    FormComponentType(Integer code, String sysType, String dingType)
    {
        this.code = code;
        this.sysType = sysType;
        this.dingType=dingType;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getSysType()
    {
        return sysType;
    }

    public String getDingType() {
        return dingType;
    }

    public static FormComponentType getType(String sysType) {
        for (FormComponentType formComponentType:FormComponentType.values()){
            if(formComponentType.getSysType().equals(sysType)){
                return formComponentType;
            }
        }
        return null;
    }

}
