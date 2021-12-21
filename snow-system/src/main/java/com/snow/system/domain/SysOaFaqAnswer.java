package com.snow.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * faq问题答案对象 sys_oa_faq_answer
 * 
 * @author 阿吉
 * @date 2021-06-11
 */
@Data
public class SysOaFaqAnswer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Integer id;
    /** faq编码 */
    private String faqNo;

    /** 问题答案 */
    @Excel(name = "问题答案")
    private String answerAnswer;

    /** 答案分数 */
    @Excel(name = "答案分数")
    private BigDecimal answerFraction;

    /** 是否采纳(0--初始化状态1--采纳，2-未采纳) */
    @Excel(name = "是否采纳(0--初始化状态1--采纳，2-未采纳)")
    private Long isAdopt;

    /** 问题点赞数 */
    @Excel(name = "问题点赞数")
    private Long answerLikes;

    /** 问题吐槽数 */
    @Excel(name = "问题吐槽数")
    private Long answerRidicule;

    /** 采纳时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "采纳时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date adoptTime;

    /** 删除标识(0--正常，1--删除) */
    private Long isDelete;


    private SysOaFaq sysOaFaq;


}
