package com.snow.flowable.domain.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/12/3 14:09
 */
@Data
public class ProcessDefinitionResponse implements Serializable {
    private static final long serialVersionUID = 3341234579178886021L;


    private String id;
    /**
     * 分类
     */
    private String category;
    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程key
     */
    private String key;
}
