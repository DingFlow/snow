package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 数字输入框
 * @Description:
 * @date 2021/11/23 17:11
 */
@Data
public class NumberInputField extends BaseField implements Serializable {
    private static final long serialVersionUID = 8150151841198080579L;

    /**
     * 宽度
     */
    private String labelWidth;
    /**
     * 宽度百分比
     */
    private String  width;

    /**
     * 数值
     */
    private Integer defaultValue;

    /**
     * 最大值
     */
    private Integer maxValue;

    /**
     * 最小值
     */
    private Integer minValue;

    /**
     * 步值
     */
    private Integer stepValue;

    /**
     * 是否只读
     */
    private boolean disabled;
    /**
     * 帮助文档
     */
    private String  document;
}
