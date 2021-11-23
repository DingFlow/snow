package com.snow.from.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-11-21 13:34
 **/
@Data
public class FormRecordRequest implements Serializable {

    private static final long serialVersionUID = 2827721348459680642L;

    @NotBlank(message = "表单id不能为空")
    private String formId;

    @NotBlank(message = "表单数据不能为空")
    private String formData;

    private String formField;
}
