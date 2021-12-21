package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 16:32
 */
@Data
public class BaseField implements Serializable {

    private static final long serialVersionUID = -6876233190306889953L;

    private String id;

    private String  index;

    private String  label;

    private String tag;

    private String  tagIcon;
}
