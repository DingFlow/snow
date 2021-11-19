package com.snow.from.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-11-18 22:08
 **/
@Data
public class FormFieldRequest implements Serializable {


    private static final long serialVersionUID = 3395398845419106008L;

    /**
     * 字段表示
     */
    private String id;

    /**
     * 字段name
     */
    private String label;

    /**
     * 字段类型
     */
    private String tag;

    /**
     * 字段类型
     */
    private String tagIcon;

    /**
     * 字段placeholder
     */
    private String placeholder;

    /**
     * 宽度
     */
    private double labelWidth;

    /**
     * 宽度百分比
     */
    private String width;

    /**
     * 最大长度
     */
    private String maxlength;

    /**
     * 是否只读
     */
    private boolean readonly;

    /**
     * 是否禁用
     */
    private boolean disabled;

    /**
     * 是否必填
     */
    private boolean required;

    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 验证表达式
     */
    private String expression;

    /**
     * 帮助文档
     */
    private String document;

    /**
     * 可选项
     */
    private String options;
}
