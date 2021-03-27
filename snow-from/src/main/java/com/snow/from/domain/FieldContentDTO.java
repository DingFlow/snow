package com.snow.from.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-21 13:49
 **/
@Data
public class FieldContentDTO implements Serializable {
    private static final long serialVersionUID = 5539925619264110377L;

    private String frame;


    private String componentKey;

    private String name;


    private String title;

    /**
     * 组件属性
     */
    private String componentProperties;

    private String html;

    private String[] children;


    private Object ortumChildren;


    private String script;

}
