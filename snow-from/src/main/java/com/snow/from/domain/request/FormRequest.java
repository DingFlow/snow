package com.snow.from.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 表单请求实体
 * @Description:
 * @date 2021/11/18 17:37
 */
@Data
public class FormRequest implements Serializable {
    private static final long serialVersionUID = 8706020335894393099L;

    @NotBlank(message = "表单id不能为空")
    private String formId;

    @NotBlank(message = "表单名称不能为空")
    private String formName;

    @NotBlank(message = "表单数据不能为空")
    private String formData;
}
