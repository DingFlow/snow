package com.snow.from.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-11-18 22:08
 **/
@Data
public class FormFieldRequest implements Serializable {

    private String id;


    private String label;

    private String tag;

    private String tagIcon;

    private boolean readonly;

    private String placeholder;

    private Boolean disabled;

    private Boolean required;


    private String defaultValue;

    private String expression;

    private String document;
}
