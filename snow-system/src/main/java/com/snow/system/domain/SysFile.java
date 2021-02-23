package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文件存储对象 sys_file
 * 
 * @author snow
 * @date 2021-01-11
 */
@Data
public class SysFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 文件的唯一索引 */
    @Excel(name = "文件的唯一索引")
    private String key;

    /** 文件名 */
    @Excel(name = "文件名")
    private String name;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String type;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long size;

    /** 文件访问链接 */
    @Excel(name = "文件访问链接")
    private String url;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    private Integer isDelete;

}
