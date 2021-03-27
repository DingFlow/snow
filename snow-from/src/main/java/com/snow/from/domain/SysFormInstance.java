package com.snow.from.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 单实例对象 sys_form_instance
 * 
 * @author 没用的阿吉
 * @date 2021-03-21
 */
@Data
public class SysFormInstance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 表单定义code */
    @Excel(name = "表单定义code")
    private String formCode;

    /** 表单名称 */
    @Excel(name = "表单名称")
    private String formName;

    /**
     * 表单内容
     */
    private String fromContentHtml;

    /** 表单url */
    @Excel(name = "表单url")
    private String fromUrl;

    /** 乐观锁版本号 */
    @Excel(name = "乐观锁版本号")
    private Long rev;

    /** 删除标识（0--正常，1--删除） */
    @Excel(name = "删除标识", readConverterExp = "0=--正常，1--删除")
    private Long isDelete;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;
}
