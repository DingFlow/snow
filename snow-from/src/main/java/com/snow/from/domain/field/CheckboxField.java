package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title: 复选框
 * @Description:
 * @date 2021/11/23 17:00
 */
@Data
public class CheckboxField extends BaseField implements Serializable {
    private static final long serialVersionUID = -4033182714436591774L;


    /**
     * 宽度
     */
    private String labelWidth;

    /**
     * 是否只读
     */
    private String disabled;
    /**
     * 是否必填
     */
    private String required;
    /**
     * 帮助文档
     */
    private String document;
    /**
     * 数据类型
     */
    private String datasourceType;
    /**
     * 远程url
     */
    private String remoteUrl;
    /**
     * 远程方法
     */
    private String remoteMethod;

    /**
     * 映射到text
     */
    private String remoteOptionText;
    /**
     * 映射到value
     */
    private String remoteOptionValue;

    /**
     * 下拉框选项
     */
    private List<Options> options;
}
