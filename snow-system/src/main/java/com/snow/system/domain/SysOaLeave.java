package com.snow.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 请假单对象 sys_oa_leave
 * 
 * @author snow
 * @date 2020-11-22
 */
@Data
public class SysOaLeave extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;
    /** 请假单号 */
    @Excel(name = "请假单号")
    private String leaveNo;
    /** 请假名称 */
    @Excel(name = "请假名称")
    private String name;

    /** 请假理由 */
    @Excel(name = "请假理由")
    private String reason;

    /** 开始时间 */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 流程状态（0--待审批，1-审批中，2--审批通过，3--已驳回） */
    @Excel(name = "流程状态", readConverterExp = "0=待审批，1=审批中，2=审批通过，3=已驳回")
    private Integer processStatus;

    /** 流程实例ID */
    @Excel(name = "流程实例ID")
    private String processInstanceId;

    /** 申请人 */
    @Excel(name = "申请人")
    private String applyPerson;

    /** 删除标识 */
    private Integer isDelete;

    /** 附件 */
    @Excel(name = "附件")
    private String fileUrl;

    /**
     * 请假时长
     */
    private String leaveTime;

}
