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
    /**
     * 初始页
     */
    private int firstResult=0;
    /**
     * 每页数
     */
    private int maxResults=10;
}
