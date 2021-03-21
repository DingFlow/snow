package com.snow.from.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-21 10:35
 **/
@Data
public class FromInfoDTO implements Serializable {


    private static final long serialVersionUID = -1727483907232851682L;

    private String id;

    /**
     * 行id
     */
    private String columnID;

    /**
     * 行名称
     */
    private String columnName;


    /**
     * html内容
     */
    private String contentHtml;


    private String  editor;


    private Date editTime;

    /**
     * from表单code
     */
    private String formCode;
    /**
     * from表单name
     */
    private Date  formName;

    /**
     * 版本号
     */
    private String version;
}
