package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 19:36
 */
@Data
public class ImageField  extends BaseField implements Serializable {
    private static final long serialVersionUID = -8971557693208690368L;
    private String placeholder;

    private String defaultValue;

    private String  width;

    private boolean  readonly;

    private boolean  disabled;

    private boolean  required;

    private String  document;

    /**
     * 上传url
     */
    private String uploadUrl;
}
