package com.snow.from.domain.field;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/23 19:32
 */
@Data
public class GridField implements Serializable {

    private static final long serialVersionUID = 3967652788696005612L;
    private String  id;
    private String index;
    private String tag;
    private int span;
    private List<Columns> columns;
}
