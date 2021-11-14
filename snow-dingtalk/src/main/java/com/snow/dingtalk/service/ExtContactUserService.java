package com.snow.dingtalk.service;

import com.dingtalk.api.response.OapiExtcontactListlabelgroupsResponse;
import com.snow.dingtalk.model.request.ExtContactUserRequest;

import java.util.List;

/**
 * @author qimingjin
 * @Title: 外部联系人
 * @Description:
 * @date 2021/11/4 9:25
 */
public interface ExtContactUserService {
   /**
    * 添加外部联系人
    * @param extContactUserRequest 请求参数
    * @return 外部联系人userId
    */
   String createExtContactUser(ExtContactUserRequest extContactUserRequest);

   /**
    * 删除外部联系人
    * @param userId 用户钉钉id
    * @return 是否成功
    */
   boolean deleteExtContactUser(String userId);

   /**
    * 编辑外部联系人
    * @param extContactUserRequest 请求参数
    * @return 是否成功
    */
   boolean updateExtContactUser(ExtContactUserRequest extContactUserRequest);

   /**
    * 获取外部联系人列表
    * @param offset
    * @param size
    * @return
    */
   List<ExtContactUserRequest> getExtContactUserList(Long offset, Long size) ;

   /**
    * 获取外部联系人详情
    * @param userId 用户id
    * @return
    */
   ExtContactUserRequest getExtContactUserDetail(String userId);

   /**
    * 获取外部联系人标签
    * @return
    */
   List<OapiExtcontactListlabelgroupsResponse.OpenLabelGroup>  getExtContactUserLabelGroupsList();
}
