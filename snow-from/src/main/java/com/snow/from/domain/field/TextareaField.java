package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 17:34
 */
@Data
public class TextareaField extends BaseField implements Serializable {
    private static final long serialVersionUID = -2547979422890331700L;

    private String placeholder;
    private String defaultValue;
    private String  width;
    private boolean  readonly;
    private boolean  disabled;
    private boolean  required;
    private String  document;
}
