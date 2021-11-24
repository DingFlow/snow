package com.snow.from.domain.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-11-24 21:56
 **/
@Data
public class BaseFormDataResponse implements Serializable {
    private static final long serialVersionUID = -5690261925196737207L;
    /**
     * 表单内容
     */
    private String formData;

    /**
     * 表单字典
     */
    private String formField;
}
