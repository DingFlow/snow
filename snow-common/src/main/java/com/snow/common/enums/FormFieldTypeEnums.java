package com.snow.common.enums;

/**
 * @author qimingjin
 * @Title: 表单类型
 * @Description:
 * @date 2021/11/19 15:34
 */
public enum FormFieldTypeEnums {
    INPUT("input","文本输入框"),

    PASSWORD("password","密码输入框"),

    TEXTAREA("textarea","文本域"),

    NUMBER_INPUT("numberInput","排序文本框"),

    SELECT("select","下拉框"),

    RADIO("radio","单选框"),

    CHECKBOX("checkbox","复选框"),

    SWITCH("switch","开关"),

    SLIDER("slider","滑块"),

    DATE("date","日期"),

    DATE_RANGE("dateRange","日期范围"),

    RATE("rate","评分"),

    CAROUSEL("carousel","轮播图"),

    COLOR_PICKER("colorpicker","颜色选择器"),

    CRON("cron","Cron表达式"),

    SIGN("sign","签名组件"),

    GRID("grid","grid"),

    IMAGE("image","图片"),

    FILE("file","文件");
    private final String code;
    private final String info;

    FormFieldTypeEnums(String code, String info)
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

    public static FormFieldTypeEnums getInfo(String info) {
        for (FormFieldTypeEnums formFieldTypeEnums:FormFieldTypeEnums.values()){
            if(formFieldTypeEnums.getInfo().equals(info)){
                return formFieldTypeEnums;
            }
        }
        return null;
    }

    public static FormFieldTypeEnums getCode(String code) {
        for (FormFieldTypeEnums formFieldTypeEnum:FormFieldTypeEnums.values()){
            if(formFieldTypeEnum.getCode().equals(code)){
                return formFieldTypeEnum;
            }
        }
        return null;
    }

}
