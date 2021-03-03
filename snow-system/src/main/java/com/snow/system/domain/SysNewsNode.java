package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import com.snow.common.core.domain.TreeEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消息配置节点对象 sys_news_node
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
@Data
public class SysNewsNode extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Integer id;

    /** 流程消息节点名称 */
    @Excel(name = "流程消息节点名称")
    private String newsNodeName;

    /** 流程消息节点key */
    @Excel(name = "流程消息节点key")
    private String newsNodeKey;

    /** 是否删除（0--否，1--是） */
    @Excel(name = "是否删除", readConverterExp = "0=--否，1--是")
    private Long isDelete;

}
