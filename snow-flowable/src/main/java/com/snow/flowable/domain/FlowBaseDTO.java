package com.snow.flowable.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/25 16:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlowBaseDTO implements Serializable {


    private static final long serialVersionUID = 9129209505175724304L;
    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    /**
     * 初始页
     */
    private int firstResult=0;
    /**
     * 每页数
     */
    private int maxResults=10;


    public Integer getPageNum() {
        return (this.pageNum - 1) * this.pageSize;
    }



    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


}
