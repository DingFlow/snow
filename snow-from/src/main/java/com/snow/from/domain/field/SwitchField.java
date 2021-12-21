package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 开关
 * @Description:
 * @date 2021/11/23 17:02
 */
@Data
public class SwitchField extends BaseField implements Serializable {
    private static final long serialVersionUID = 5405710592916631615L;

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
    private boolean switchValue;

    private boolean showWordLimit;
    /**
     * 是否只读
     */
    private boolean disabled;
    /**
     * 帮助文档
     */
    private String  document;
}
