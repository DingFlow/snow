package com.snow.system.domain;

import com.snow.common.annotation.Excel;
import com.snow.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

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

    /** id */
    private Long id;

    /** 生产者id */
    private String producerId;

    private SysUser producerUser;

    /** 消费者id */
    private String consumerId;

    private SysUser consumerUser;

    /** 消息类型 */
    private String messageType;

    /** 消息外部id */
    private String messageOutsideId;
    /**
     * 模板code
     */
    private String templateCode;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 消息展示1app端 2pc端 3官网 4后台
     */
    private Integer messageShow;

    /**
     * 消息pc端url
     */
    private String pcUrl;

    /**
     * 消息app端url
     */
    private String appUrl;

    /** 0--正常，1--禁用 */
    private Long messageStatus;

    /** 0--未读，1--已读 */
    private Long messageReadStatus;

    /** 0--正常，1--删除 */
    private Long isDelete;


    private String producerOrConsumerId;

    /**
     * 跳转URL
     */
    private String redirectUrl;

    /**
     * 图标样式
     */
    private String iconClass;

    /**
     * 已用时
     */
    private String spendTime;

    /**
     * 模板
     */
    private SysMessageTemplate sysMessageTemplate;


    public static void init(List<SysMessageTransition> sysMessageTransitions){
        sysMessageTransitions.forEach((t)->{
            switch (t.getMessageType()){
                case "SEND_EMAIL":
                    t.setIconClass("fa fa-envelope fa-fw");
                    t.setRedirectUrl("/system/email/mailDetail/"+t.getMessageOutsideId());
                    break;
                default:
                    t.setIconClass("fa fa-twitter fa-fw");
            }

        });

    }
}
