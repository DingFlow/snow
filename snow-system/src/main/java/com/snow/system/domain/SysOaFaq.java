package com.snow.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * faq问题对象 sys_oa_faq
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
@Data
public class SysOaFaq extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** faq编码 */
    private String faqNo;

    /** 问题标题 */
    @Excel(name = "问题标题")
    private String faqTitle;

    /** 问题类型 */
    @Excel(name = "问题类型")
    private Long faqType;

    /** 问题来源方（1-DingFlow官网） */
    private Long faqSource;

    /** 完结时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "完结时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 任务状态（1--待回答，2--已回答（有一条答案即代表已回答），3--已完结）
     */
    private Integer faqStatus;

    /** 删除标识(0--正常，1--删除) */
    private Long isDelete;

    private List<SysOaFaqAnswer> SysOaFaqAnswerList;

    private List<SysOaFaqLabel> sysOaFaqLabelList;


}
