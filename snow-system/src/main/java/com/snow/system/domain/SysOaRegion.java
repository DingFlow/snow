package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.TreeEntity;
import lombok.Data;

/**
 * 地区对象 sys_oa_region
 * 
 * @author 没用的阿吉
 * @date 2021-04-09
 */
@Data
public class SysOaRegion extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 地区代码 */
    private Long code;

    /** 上级地区代码 */
    @Excel(name = "上级地区代码")
    private Long pCode;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String name;

}
