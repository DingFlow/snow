package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 系统序列设置对象 sys_sequence
 * 
 * @author snow
 * @date 2020-11-23
 */
public class SysSequence extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序列名称 */
    private String name;

    /** 当前值 */
    @Excel(name = "当前值")
    private Long currentValue;

    /** 步幅 */
    @Excel(name = "步幅")
    private Long increment;
    @Excel(name = "描述")
    private String described;

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setCurrentValue(Long currentValue) 
    {
        this.currentValue = currentValue;
    }

    public Long getCurrentValue() 
    {
        return currentValue;
    }
    public void setIncrement(Long increment) 
    {
        this.increment = increment;
    }

    public Long getIncrement() 
    {
        return increment;
    }

    public String getDescribed() {
        return described;
    }

    public void setDescribed(String described) {
        this.described = described;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("name", getName())
            .append("currentValue", getCurrentValue())
            .append("increment", getIncrement())
            .toString();
    }
}
