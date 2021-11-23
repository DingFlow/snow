package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 17:36
 */
@Data
public class FileField extends BaseField implements Serializable {
    private static final long serialVersionUID = 4329705307452431635L;

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
