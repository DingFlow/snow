package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * faq问题标签对象 sys_oa_faq_label
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
@Data
public class SysOaFaqLabel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Integer id;
    /** faq编码 */
    private String faqNo;

    /** 问题标签 */
    @Excel(name = "问题标签")
    private String faqLabel;

    /** 删除标识(0--正常，1--删除) */
    @Excel(name = "删除标识(0--正常，1--删除)")
    private Long isDelete;

}
