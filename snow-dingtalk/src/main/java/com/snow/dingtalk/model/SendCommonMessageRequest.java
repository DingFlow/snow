package com.snow.dingtalk.model;

import com.snow.common.enums.DingTalkMessageType;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: snow
 * @description 发送消息请求类
 * @author: 没用的阿吉
 * @create: 2021-03-06 15:54
 **/
@Data
public class SendCommonMessageRequest implements Serializable {


    /**
     * 模板Code
     */
    private String templateByCode;
    /**
     * 消息
     */
    private String msg;
    /**
     * 是否发给所有人
     */
    private Boolean toAllUser=false;

    /**
     * 钉钉用户id
     */
    private String userIdList;

    /**
     * 消息类型
     */
    private DingTalkMessageType dingTalkMessageType;

    /**
     * 消息URL
     */
    private String messageUrl;

    /**
     * 图片url
     */
    private String picUrl;


    /**
     * 标题
     */
    private String title;
}
