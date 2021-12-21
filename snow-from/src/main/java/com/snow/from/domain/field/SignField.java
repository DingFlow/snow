package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 签名组件
 * @Description:
 * @date 2021/11/23 17:13
 */
@Data
public class SignField extends BaseField implements Serializable {
    private static final long serialVersionUID = 4503585715635964734L;

    /**
     * 宽度
     */
    private String labelWidth;

    /**
     * 按钮值
     */
    private String buttonVlaue;

    /**
     * 按钮icon
     */
    private String buttonIcon;

    /**
     * data
     */
    private String data;

    /**
     * 是否只读
     */
    private boolean disabled;
    /**
     * 帮助文档
     */
    private String  document;
}
