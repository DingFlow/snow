package com.snow.system.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-04-09 14:39
 **/
@Data
public class RegionTreeVO implements Serializable {
    private static final long serialVersionUID = 8263364392376570390L;

    /** 地区代码 */
    private Long code;


    private Long pCode;


    private String name;

    private List<RegionTreeVO> children;
}
