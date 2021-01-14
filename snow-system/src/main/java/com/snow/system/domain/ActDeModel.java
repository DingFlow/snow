package com.snow.system.domain;

import java.util.Date;
import java.util.List;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设计器model对象 act_de_model
 * 
 * @author qimingjin
 * @date 2020-12-01
 */
@Data
public class ActDeModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    private String id;


    @Excel(name = "模型名称")
    private String name;


    @Excel(name = "模型key")
    private String modelKey;


    @Excel(name = "描述")
    private String description;


    @Excel(name = "模型注解")
    private String modelComment;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date created;


    @Excel(name = "创建人")
    private String createdBy;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastUpdated;


    @Excel(name = "更新人")
    private String lastUpdatedBy;


    @Excel(name = "版本")
    private Long version;


    @Excel(name = "模型采用json格式")
    private String modelEditorJson;


    @Excel(name = "图片流")
    private byte[] thumbnail;

    /**
     * 详见：org.flowable.ui.modeler.domain.AbstractModel
     */
    @Excel(name = "模型类型")
    private Long modelType;


    @Excel(name = "租户ID")
    private String tenantId;

    private List modelTypeList;

    public static final int MODEL_TYPE_BPMN = 0;

    public static final int MODEL_TYPE_FORM = 2;

    public static final int MODEL_TYPE_APP = 3;

    public static final int MODEL_TYPE_DECISION_TABLE = 4;

    public static final int MODEL_TYPE_CMMN = 5;

    public static final int MODEL_TYPE_DECISION_SERVICE = 6;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("modelKey", getModelKey())
            .append("description", getDescription())
            .append("modelComment", getModelComment())
            .append("created", getCreated())
            .append("createdBy", getCreatedBy())
            .append("lastUpdated", getLastUpdated())
            .append("lastUpdatedBy", getLastUpdatedBy())
            .append("version", getVersion())
            .append("modelEditorJson", getModelEditorJson())
            .append("thumbnail", getThumbnail())
            .append("modelType", getModelType())
            .append("tenantId", getTenantId())
            .toString();
    }
}
