package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 密码输入框
 * @Description:
 * @date 2021/11/23 16:32
 */
@Data
public class PassWordField extends BaseField implements Serializable {

    private static final long serialVersionUID = -4855880096097833482L;

    private String placeholder;

    private String defaultValue;

    private String labelWidth;

    private String width;

    private String clearable;

    private String maxlength;

    private boolean showWordLimit;

    private String readonly;

    private String disabled;

    private String required;


    private String document;
}
