package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 系统任务对象 sys_oa_task
 * 
 * @author 没用的阿吉
 * @date 2021-07-29
 */
@Data
public class SysOaTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务编号 */
    private String taskNo;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 任务内容 */
    @Excel(name = "任务内容")
    private String taskContent;

    /** 任务状态（详见数据字典） */
    @Excel(name = "任务状态", readConverterExp = "详=见数据字典")
    private String taskStatus;

    /** 任务类型（详见数据字典） */
    @Excel(name = "任务类型", readConverterExp = "详=见数据字典")
    private String taskType;

    /** 任务来源 */
    @Excel(name = "任务来源")
    private String taskSource;

    /** 任务外部id */
    @Excel(name = "任务外部id")
    private String taskOutsideId;

    /** 任务跳转URL */
    @Excel(name = "任务跳转URL")
    private String taskUrl;

    /** 乐观锁 */
    @Excel(name = "乐观锁")
    private Long revision;

    /** 删除标识 */
    @Excel(name = "删除标识")
    private Long isDelete;

}
