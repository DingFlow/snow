package com.snow.from.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 单字段对象 sys_form_field
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
@Data
public class SysFormField extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 字段标识 */
    private String fieldKey;

    /** 字段名称 */
    private String fieldName;

    /** 字段类型 */
    private String fieldType;

    /** 字段html */
    private String fieldHtml;
    /**
     * 字段placeholder
     */
    private String placeholder;

    /**
     * 宽度
     */
    private double labelWidth;

    /**
     * 宽度百分比
     */
    private String width;

    /**
     * 最大长度
     */
    private String maxlength;

    /**
     * 是否只读
     */
    private boolean readonly;

    /**
     * 是否禁用
     */
    private boolean disabled;

    /**
     * 是否必填
     */
    private boolean required;

    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 验证表达式
     */
    private String expression;

    /**
     * 帮助文档
     */
    private String document;

    /**
     * 可选项
     */
    private String options;

    /** 删除标识（0--正常，1--删除） */
    private Long isDelete;

    private Long fromId;
}
