package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 17:22
 */
@Data
public class DateField extends BaseField implements Serializable {
    private static final long serialVersionUID = 8453685053675545671L;

    private String datetype;

    private boolean rang;

    private String  dateformat;

    private boolean isInitValue;

    private String  dataMaxValue;

    private String  dataMinValue;

    private String trigger;

    private String position;

    private String  theme;

    private String  mark;

    private boolean showBottom;

    private long zindex;

    private boolean disabled;

    private boolean required;

    private String document;

    /**
     * 日期默认值
     */
    private String dateDefaultValue;

    private String labelWidth;

    private String width;

    private boolean clearable;

    private String maxlength;

    private String readonly;

}
