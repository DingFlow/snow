package com.snow.dingtalk.service;

import com.dingtalk.api.response.OapiWorkrecordGetbyuseridResponse;
import com.snow.dingtalk.model.WorkrecordAddRequest;
import com.snow.dingtalk.model.WorkrecordGetbyuseridRequest;
import com.snow.framework.web.domain.common.SysSendMessageDTO;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/21 14:28
 */
@Service
public interface WorkRecodeService  {

    /**
     * 创建工作待办
     * @param workrecordAddRequest
     * @return
     */
     String create(WorkrecordAddRequest workrecordAddRequest);

    /**
     * 根据用户ID获取待办
     * @param workrecordGetbyuseridRequest
     * @return
     */
     OapiWorkrecordGetbyuseridResponse.PageResult getWorkRecordByUserId(WorkrecordGetbyuseridRequest workrecordGetbyuseridRequest);

    /**
     * 更新待办
     * @param userId
     * @param recordId
     * @return
     */
     Boolean update(String userId,String recordId);

    /**
     * 钉钉发送消息
     * @param sysSendMessageDTO
     */
    Long sendCommonMessage(SysSendMessageDTO sysSendMessageDTO);
}
