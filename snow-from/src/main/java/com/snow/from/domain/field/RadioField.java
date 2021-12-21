package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 16:53
 */
@Data
public class RadioField extends BaseField implements Serializable {
    private static final long serialVersionUID = 7019933043982662705L;
    /**
     * 宽度
     */
    private String labelWidth;
    /**
     * 是否只读
     */
    private String disabled;
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
