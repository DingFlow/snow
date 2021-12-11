package com.snow.from.domain.response;

import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 9:26
 */
@Data
public class FormDataRecordResponse extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5569500779097541889L;
    /** 主键ID */
    private Integer id;

    /** 表单定义code */
    private String formId;

    /**
     * 表单编号
     */
    private String formNo;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单内容
     */
    private String formData;

    /**
     * 表单字典
     */
    private String formField;

    /** 表单url */
    private String formUrl;

    /** 表单操作人所属人id */
    private String belongUserId;

    /** 表单操作人所属人名称 */
    private String belongUserName;

    /** 版本号 */
    private Integer version;

    /**
     * 表单状态
     */
    private String formStatus;
    /**
     * 流程标识
     */
    private Boolean processFlag=false;
}
