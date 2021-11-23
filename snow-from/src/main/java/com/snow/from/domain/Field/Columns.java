package com.snow.from.domain.Field;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 19:33
 */
@Data
public class Columns implements Serializable {
    private static final long serialVersionUID = -9043104136356462576L;

    private int span;

    private List list;
}
