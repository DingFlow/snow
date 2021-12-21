package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 评分
 * @Description:
 * @date 2021/11/23 17:30
 */
@Data
public class RateField extends BaseField implements Serializable {
    private static final long serialVersionUID = -4822301448776454122L;
    private int labelWidth;
    /**
     * 默认值
     */
    private int defaultValue;
    /**
     * 星星长度
     */
    private int rateLength;
    private boolean half;
    private boolean text;
    private String theme;
    private boolean showBottom;
    private boolean readonly;
    private String document;
}
