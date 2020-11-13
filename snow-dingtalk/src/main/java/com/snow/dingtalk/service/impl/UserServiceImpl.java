package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserCreateRequest;
import com.dingtalk.api.request.OapiV2UserDeleteRequest;
import com.dingtalk.api.response.OapiV2UserCreateResponse;
import com.dingtalk.api.response.OapiV2UserDeleteResponse;
import com.snow.common.annotation.DingTalkSyncLog;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.exception.DingTalkSyncException;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.service.UserService;
import com.snow.system.domain.SysPost;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysDeptServiceImpl;
import com.snow.system.service.impl.SysPostServiceImpl;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/9 10:51
 */
@Service
@Slf4j
public class UserServiceImpl  extends BaseService implements UserService {

    private SysPostServiceImpl sysPostService=SpringUtils.getBean("sysPostServiceImpl");

    @Override
    @DingTalkSyncLog(dingTalkListenerType = DingTalkListenerType.USER_CREATE,dingTalkUrl=BaseConstantUrl.USER_CREATE)
    public OapiV2UserCreateResponse.UserCreateResponse createUser(SysUser sysUser) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.USER_CREATE);
        OapiV2UserCreateRequest req = new OapiV2UserCreateRequest();
        req.setUserid(String.valueOf(sysUser.getUserId()));
        req.setName(sysUser.getUserName());
        req.setMobile(sysUser.getPhonenumber());
        req.setHideMobile(false);
        req.setTelephone(sysUser.getPhonenumber());
        req.setJobNumber(sysUser.getJobnumber());
        req.setTitle(sysUser.getPosition());
        req.setEmail(sysUser.getEmail());
        req.setOrgEmail(sysUser.getOrgEmail());
        req.setWorkPlace(sysUser.getWorkPlace());
        req.setRemark(sysUser.getRemark());
        //部门list
        req.setDeptIdList(JSON.toJSONString(sysUser.getDept().getDeptId()));
        List<OapiV2UserCreateRequest.DeptOrder> list2 = new ArrayList<>();
        //员工在对应的部门中的职位
        List<OapiV2UserCreateRequest.DeptTitle> deptTitleList = new ArrayList<>();
        Long[] postIds = sysUser.getPostIds();
        if(StringUtils.isNotNull(postIds)){
            for(Long postId:postIds){
                SysPost sysPost = sysPostService.selectPostById(postId);
                OapiV2UserCreateRequest.DeptTitle deptTitle = new OapiV2UserCreateRequest.DeptTitle();
                deptTitle.setDeptId(sysUser.getDeptId());
                deptTitle.setTitle(sysPost.getPostName());
                deptTitleList.add(deptTitle);
            }
        }

        //req.setExtension("{\"爱好\":\"旅游\",\"年龄\":\"24\"}");
        req.setSeniorMode(false);
        req.setHiredDate(sysUser.getHiredDate());
        OapiV2UserCreateResponse response = null;
        try {
            response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()==0){
                OapiV2UserCreateResponse.UserCreateResponse result = response.getResult();
                return result;
            }else {
                throw new DingTalkSyncException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("钉钉createUser异常：{}",e.getErrMsg());
            throw new DingTalkSyncException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkSyncLog(dingTalkListenerType = DingTalkListenerType.USER_DELETE,dingTalkUrl=BaseConstantUrl.USER_DELETE)
    public void deleteUser(String ids) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.USER_DELETE);
        OapiV2UserDeleteRequest req = new OapiV2UserDeleteRequest();
        req.setUserid(ids);
        OapiV2UserDeleteResponse response = null;
        try {
            response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()==0){
                String requestId = response.getRequestId();
                log.info("dingTalk删除用户返回：{}",requestId);
            }else {
                throw new DingTalkSyncException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("钉钉deleteUser异常：{}",e.getErrMsg());
            e.printStackTrace();
            throw new DingTalkSyncException(JSON.toJSONString(req),e.getErrMsg());
        }
    }
}
