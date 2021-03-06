package com.snow.framework.web.domain.common;

import com.snow.common.enums.DingTalkMessageType;
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
public class SysSendMessageDTO implements Serializable {


    private static final long serialVersionUID = 9148998626002082665L;
    /**
     * 模板code
     */
    private String templateByCode;


    /**
     * 参数List
     */
    private Map<String,String> paramMap;

    /**
     * 接收人
     */
    private String receiver;
    /**
     * 接收人set集合
     */
    private Set receiverSet;
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

}
