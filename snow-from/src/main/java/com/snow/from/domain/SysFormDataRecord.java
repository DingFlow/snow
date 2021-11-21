package com.snow.from.domain;

import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 单数据记录对象 sys_form_data_record
 * 
 * @author 阿吉
 * @date 2021-11-21
 */
@Data
public class SysFormDataRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Integer id;

    /** 表单定义code */
    private String formId;

    /**
     * 表单内容
     */
    private String formData;

    /** 表单url */
    private String formUrl;

    /** 表单操作人所属人id */
    private String belongUserId;

    /** 版本号 */
    private Integer version;
}
