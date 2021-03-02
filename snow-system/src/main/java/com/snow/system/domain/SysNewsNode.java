package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import com.snow.common.core.domain.TreeEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消息配置节点对象 sys_news_node
 * 
 * @author qimingjin
 * @date 2021-03-02
 */
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

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setNewsNodeName(String newsNodeName) 
    {
        this.newsNodeName = newsNodeName;
    }

    public String getNewsNodeName() 
    {
        return newsNodeName;
    }
    public void setNewsNodeKey(String newsNodeKey) 
    {
        this.newsNodeKey = newsNodeKey;
    }

    public String getNewsNodeKey() 
    {
        return newsNodeKey;
    }
    public void setIsDelete(Long isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Long getIsDelete() 
    {
        return isDelete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("newsNodeName", getNewsNodeName())
            .append("newsNodeKey", getNewsNodeKey())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("orderNum", getOrderNum())
            .append("isDelete", getIsDelete())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
