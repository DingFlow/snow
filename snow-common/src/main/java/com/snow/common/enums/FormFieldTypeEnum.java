package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title: 表单类型
 * @Description:
 * @date 2021/11/19 15:34
 */
public enum FormFieldTypeEnum {
    INPUT("input","TextField","文本输入框"),

    PASSWORD("password","","密码输入框"),

    TEXTAREA("textarea","TextareaField","文本域"),

    NUMBER_INPUT("numberInput","NumberField","排序(数字)文本框"),

    SELECT("select","","下拉框"),

    RADIO("radio","DDSelectField","单选框"),

    CHECKBOX("checkbox","DDMultiSelectField","复选框"),

    SWITCH("switch","","开关"),

    SLIDER("slider","","滑块"),

    DATE("date","DDDateField","日期"),

    DATE_RANGE("dateRange","DDDateRangeField","日期范围"),

    RATE("rate","","评分"),

    CAROUSEL("carousel","","轮播图"),

    COLOR_PICKER("colorpicker","","颜色选择器"),

    ICON_PICKER("iconPicker","","颜色选择器"),

    CRON("cron","","Cron表达式"),

    SIGN("sign","","签名组件"),

    GRID("grid","","grid"),

    IMAGE("image","","图片"),

    FILE("file","","文件");
    //系统组件code
    private final String code;
    //钉钉组件code
    private final String dingTalkCode;
    //组件信息
    private final String info;

    FormFieldTypeEnum(String code, String dingTalkCode, String info)
    {
        this.code = code;
        this.dingTalkCode = dingTalkCode;
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

    public String getDingTalkCode() {
        return dingTalkCode;
    }

    public static FormFieldTypeEnum getInfo(String info) {
        for (FormFieldTypeEnum formFieldTypeEnums: FormFieldTypeEnum.values()){
            if(formFieldTypeEnums.getInfo().equals(info)){
                return formFieldTypeEnums;
            }
        }
        return null;
    }

    public static FormFieldTypeEnum getCode(String code) {
        for (FormFieldTypeEnum formFieldTypeEnum: FormFieldTypeEnum.values()){
            if(formFieldTypeEnum.getCode().equals(code)){
                return formFieldTypeEnum;
            }
        }
        return null;
    }

    public static FormFieldTypeEnum getDingTalkCode(String dingTalkCode) {
        for (FormFieldTypeEnum formFieldTypeEnum: FormFieldTypeEnum.values()){
            if(formFieldTypeEnum.getDingTalkCode().equals(dingTalkCode)){
                return formFieldTypeEnum;
            }
        }
        return null;
    }

}
