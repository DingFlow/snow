package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 滑块
 * @Description:
 * @date 2021/11/23 17:08
 */
@Data
public class SliderField extends BaseField implements Serializable {
    private static final long serialVersionUID = -3032040915441717608L;
    /**
     * 宽度
     */
    private String labelWidth;
    /**
     * 宽度百分比
     */
    private String  width;

    /**
     * 开关值（true-开，false-关）
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

    private boolean isInput;
    /**
     * 是否只读
     */
    private boolean disabled;
    /**
     * 帮助文档
     */
    private String  document;
}
