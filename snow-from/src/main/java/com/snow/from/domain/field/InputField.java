package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: input输入框
 * @Description:
 * @date 2021/11/23 16:32
 */
@Data
public class InputField extends BaseField implements Serializable {

    private static final long serialVersionUID = -2408672020265728342L;

    private String placeholder;

    private String defaultValue;

    private String labelWidth;

    private String width;

    private boolean clearable;

    private String maxlength;

    private boolean showWordLimit;

    private String readonly;

    private String disabled;

    private String required;

    private String  expression;

    private String document;
}
