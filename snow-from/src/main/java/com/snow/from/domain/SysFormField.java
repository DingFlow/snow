package com.snow.from.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    private Long id;

    /** 字段标识 */
    @Excel(name = "字段标识")
    private String fieldKey;

    /** 字段名称 */
    @Excel(name = "字段名称")
    private String fieldName;

    /** 字段类型 */
    @Excel(name = "字段类型")
    private String fieldType;

    /** 字段html */
    @Excel(name = "字段html")
    private String fieldHtml;

    /** 乐观锁版本号 */
    @Excel(name = "乐观锁版本号")
    private Long rev;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 删除标识（0--正常，1--删除） */
    @Excel(name = "删除标识", readConverterExp = "0=--正常，1--删除")
    private Long isDelete;

    private Long fromId;
}
