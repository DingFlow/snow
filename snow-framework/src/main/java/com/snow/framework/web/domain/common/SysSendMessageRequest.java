package com.snow.framework.web.domain.common;

import com.snow.common.enums.DingTalkMessageType;
import com.snow.common.enums.MessageEventType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @program: snow
 * @description 发送信息实体
 * @author: 没用的阿吉
 * @create: 2021-03-01 16:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SysSendMessageRequest implements Serializable {


    private static final long serialVersionUID = 9148998626002082665L;
    /**
     * 模板code(默
     */
    private String templateByCode;


    /**
     * 发送人
     */
    private String from;
    /**
     * 参数List
     */
    private Map<String,Object> paramMap;

    /**
     * 接收人
     */
    private String receiver;
    /**
     * 接收人set集合
     */
    private Set<String> receiverSet;
    /**
     * 抄送人
     */
    private Set CCSet;

    /**
     * 文件path
     */
    private String filePath;

    /**
     * 发送时间
     */
    private Date sentDate;


    /**
     * 是否发给所有人
     */
    private Boolean toAllUser=false;


    /**
     * 消息类型
     */
    private DingTalkMessageType dingTalkMessageType;

    /**
     * pc端url
     */
    private String pcUrl;

    /**
     * app端url
     */
    private String appUrl;

    /**
     * 1--官网,2--后台
     */
    private Integer messageShow;

    /**
     * 外部消息id
     */
    private String messageOutsideId;

    /**
     * 消息事件类型
     */
    private MessageEventType messageEventType;
}
