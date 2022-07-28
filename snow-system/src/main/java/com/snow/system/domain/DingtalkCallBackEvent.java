package com.snow.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 事件表对象 dingtalk_call_back_event
 * 
 * @author snow
 * @date 2020-11-03
 */
@Data
public class DingtalkCallBackEvent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 事假名称
     */
    private String eventName;


    /**
     * 主表回调id
     */
    private Long callBackId;

    /**
     * 时间描述
     */
    private String eventDesc;


    @TableLogic
    private Long delFlag;

}
