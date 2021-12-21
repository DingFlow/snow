package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 16:48
 */
@Data
public class Options implements Serializable {

    private static final long serialVersionUID = 975771972944583123L;
    /**
     * 文本
     */
    private String text;
    /**
     * 值
     */
    private String value;
    /**
     * 是否选中
     */
    private boolean checked;
}
