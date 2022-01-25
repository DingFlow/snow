package com.snow.dingtalk.service;

import com.snow.framework.web.domain.common.SysSendMessageRequest;

/**
 * @author qimingjin
 * @Title: 钉钉消息服务
 * @Description:
 * @date 2021/11/12 13:22
 */
public interface MessageService {

    /**
     * 发送工作通知消息
     * @param sysSendMessageDTO 请求参数
     * @return 任务id
     */
    Long sendWorkNotice(SysSendMessageRequest sysSendMessageDTO);

}
