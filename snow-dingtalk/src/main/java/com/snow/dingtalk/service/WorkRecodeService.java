package com.snow.dingtalk.service;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.request.OapiWorkrecordGetbyuseridRequest;
import com.dingtalk.api.request.OapiWorkrecordUpdateRequest;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.dingtalk.api.response.OapiWorkrecordGetbyuseridResponse;
import com.dingtalk.api.response.OapiWorkrecordUpdateResponse;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.WorkrecordAddRequest;
import com.snow.dingtalk.model.WorkrecordGetbyuseridRequest;
import com.snow.system.service.ISysConfigService;
import com.taobao.api.ApiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
}
