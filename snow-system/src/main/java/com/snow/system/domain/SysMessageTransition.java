package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消息流转中心对象 sys_message_transition
 * 
 * @author 没用的阿吉
 * @date 2021-03-30
 */
@Data
public class SysMessageTransition extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Long id;

    /** 生产者id */
    @Excel(name = "生产者id")
    private String producerId;

    private SysUser producerUser;

    /** 消费者id */
    @Excel(name = "消费者id")
    private String consumerId;

    private SysUser consumerUser;

    /** 消息类型 */
    @Excel(name = "消息类型")
    private String messageType;

    /** 消息外部id */
    @Excel(name = "消息外部id")
    private String messageOutsideId;

    /** 0--正常，1--禁用 */
    @Excel(name = "0--正常，1--禁用")
    private Long messageStatus;

    /** 0--未读，1--已读 */
    @Excel(name = "0--未读，1--已读")
    private Long messageReadStatus;

    /** 0--正常，1--删除 */
    @Excel(name = "0--正常，1--删除")
    private Long isDelete;


    private String producerOrConsumerId;
}
