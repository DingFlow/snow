package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title: cron表达式
 * @Description:
 * @date 2021/11/23 17:16
 */
@Data
public class CronField extends BaseField implements Serializable {
    private static final long serialVersionUID = -8279586363040232466L;

    /**
     * 宽度
     */
    private String labelWidth;

    /**
     * 宽度百分比
     */
    private String width;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * placeholder
     */
    private String placeholder;

    /**
     * cronUrl
     */
    private String cronUrl;

    /**
     * 是否只读
     */
    private boolean disabled;
    /**
     * 是否必填
     */
    private boolean required;
    /**
     * 帮助文档
     */
    private String  document;
}
