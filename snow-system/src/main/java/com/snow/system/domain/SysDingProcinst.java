package com.snow.system.domain;

import java.util.Date;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 钉钉流程实例对象 sys_ding_procinst
 * 
 * @author 没用的阿吉
 * @date 2021-03-24
 */
@Data
public class SysDingProcinst extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 版本号 */
    @Excel(name = "版本号")
    private Long rev;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String procInstId;

    /** 业务参数 */
    @Excel(name = "业务参数")
    private String businessKey;

    /** 审批实例对应的企业 */
    @Excel(name = "审批实例对应的企业")
    private String corpId;

    /** 流程开始时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "流程开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 流程结束时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "流程结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date finishTime;

    /** 事件类型 */
    @Excel(name = "事件类型")
    private String eventType;

    /** finish：审批正常结束（同意或拒绝）terminate：审批终止（发起人撤销审批单） */
    @Excel(name = "finish：审批正常结束", readConverterExp = "同=意或拒绝")
    private String type;

    /** 实例标题 */
    @Excel(name = "实例标题")
    private String title;

    /** 审批模板的唯一码 */
    @Excel(name = "审批模板的唯一码")
    private String procCode;

    /** 租户id */
    @Excel(name = "租户id")
    private String tenantId;

    /** null */
    @Excel(name = "null")
    private String name;

    /** 审批实例url，可在钉钉内跳转到审批页面 */
    @Excel(name = "审批实例url，可在钉钉内跳转到审批页面")
    private String dingUrl;

    /** 正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值 */
    @Excel(name = "正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值")
    private String procResult;


    /**
     * 流程发起人钉钉ID
     */
    private String startUserId;


    /**
     *流程发起人名字
     */
    private String startUserName;

    /**
     * 流程用时
     */
    private String processSpendTime;
}
