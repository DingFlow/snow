package com.snow.from.domain.response;

import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: 表单实例返回
 * @Description:
 * @date 2021/11/23 10:45
 */
@Data
public class FormInstanceResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3712210141375254841L;

    /** 主键ID */
    private Long id;

    /** 表单定义code */
    private String formCode;

    /** 表单名称 */
    private String formName;

    /**
     * 表单内容
     */
    private String fromContentHtml;

    /** 表单url */
    private String fromUrl;

    /**
     * 创建人名称
     */
    private String createByName;

}
