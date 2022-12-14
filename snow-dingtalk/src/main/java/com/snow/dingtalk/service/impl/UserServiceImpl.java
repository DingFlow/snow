package com.snow.dingtalk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.request.DingUserCreateRequest;
import com.snow.dingtalk.model.client.SnowDingTalkDefaultClient;
import com.snow.dingtalk.model.client.SnowDingTalkClient;
import com.snow.dingtalk.model.request.UserListRequest;
import com.snow.dingtalk.service.UserService;
import com.snow.system.domain.SysPost;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysUserService;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.snow.system.service.impl.SysPostServiceImpl;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    private SysPostServiceImpl sysPostService=SpringUtils.getBean(SysPostServiceImpl.class);

    private SysConfigServiceImpl isysConfigService=SpringUtils.getBean(SysConfigServiceImpl.class);

    private ISysUserService sysUserService=SpringUtils.getBean(ISysUserService.class);


    @Override
    @DingTalkLog(dingTalkLogType=DingTalkLogType.USER_CREATE,dingTalkUrl =BaseConstantUrl.USER_CREATE)
    public String createDingUser(DingUserCreateRequest createRequest) {
        OapiV2UserCreateRequest oapiV2UserCreateRequest = BeanUtil.toBean(createRequest, OapiV2UserCreateRequest.class);
        try {
            OapiV2UserCreateResponse rsp = new SnowDingTalkDefaultClient<OapiV2UserCreateResponse>().execute(oapiV2UserCreateRequest,BaseConstantUrl.USER_CREATE);
            if(rsp.getErrcode()!=0){
                log.error("@@???????????????????????????????????????{}",rsp.getErrmsg());
                throw new SyncDataException(JSON.toJSONString(oapiV2UserCreateRequest),rsp.getErrmsg());
            }
            return rsp.getResult().getUserid();
        } catch (ApiException e) {
            log.error("@@???????????????????????????????????????{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(oapiV2UserCreateRequest),e.getErrMsg());
        }
    }

    public OapiSnsGetuserinfoBycodeResponse.UserInfo getUserInfoByCode(String authCode) {
        String appId= isysConfigService.selectConfigByKey("ding.login.appid");
        String appSecret= isysConfigService.selectConfigByKey("ding.login.appSecret");
        // ??????????????????????????????????????????????????????
        DefaultDingTalkClient client2 = new DefaultDingTalkClient(BaseConstantUrl.GET_USER_BY_CODE);
        OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
        // ???????????????????????????????????????redirect_uri?????????url????????????code???????????????
        req.setTmpAuthCode(authCode);
        OapiSnsGetuserinfoBycodeResponse bycodeResponse = null;

        try {
            bycodeResponse = client2.execute(req, appId,appSecret);
            if(bycodeResponse.getErrcode()==0){
                OapiSnsGetuserinfoBycodeResponse.UserInfo userInfo = bycodeResponse.getUserInfo();
                return userInfo;
            }else {
                throw new SyncDataException(JSON.toJSONString(req),bycodeResponse.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@???????????????????????????????????????????????????????????????{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }
    /**
     * ??????????????????
     * @param sysUser
     * @return
     */
    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.USER_CREATE,dingTalkUrl=BaseConstantUrl.USER_CREATE)
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
        //??????list
        req.setDeptIdList(JSON.toJSONString(sysUser.getDeptId()));
        //????????????????????????????????????
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
        req.setSeniorMode(false);
        if(StringUtils.isNotNull(sysUser.getHiredDate())){
            Date hiredDate= sysUser.getHiredDate();
            req.setHiredDate(hiredDate.getTime());
        }
        OapiV2UserCreateResponse response = null;
        try {
            response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()==0){
                OapiV2UserCreateResponse.UserCreateResponse result = response.getResult();
                return result;
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@??????createUser?????????{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.USER_UPDATE,dingTalkUrl=BaseConstantUrl.USER_UPDATE)
    public String updateUserV2(SysUser sysUser) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.USER_UPDATE);
        OapiV2UserUpdateRequest req = new OapiV2UserUpdateRequest();
        req.setUserid(sysUser.getDingUserId());
        req.setName(sysUser.getUserName());
        req.setMobile(sysUser.getPhonenumber());
        req.setHideMobile(false);
        req.setJobNumber(sysUser.getJobnumber());
        req.setTitle(sysUser.getPosition());
        req.setEmail(sysUser.getEmail());
        req.setOrgEmail(sysUser.getOrgEmail());
        req.setWorkPlace(sysUser.getWorkPlace());
        req.setRemark(sysUser.getRemark());
        req.setDeptIdList(JSON.toJSONString(sysUser.getDeptId()));
        req.setSeniorMode(false);
        if(ObjectUtil.isNotNull(sysUser.getHiredDate())){
            req.setHiredDate(sysUser.getHiredDate().getTime());
        }
        req.setLanguage(Constants.ZH_CN);
        try {
            OapiV2UserUpdateResponse response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()==0){
                return response.getRequestId();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@??????updateUser?????????{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }


    /**
     * ??????????????????
     * @param ids
     */
    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.USER_DELETE,dingTalkUrl=BaseConstantUrl.USER_DELETE)
    public void deleteUser(String ids) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.USER_DELETE);
        OapiV2UserDeleteRequest req = new OapiV2UserDeleteRequest();
        req.setUserid(ids);
        OapiV2UserDeleteResponse response = null;
        try {
            response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()==0){
                String requestId = response.getRequestId();
                log.info("dingTalk?????????????????????{}",requestId);
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("??????deleteUser?????????{}",e.getErrMsg());
            e.printStackTrace();
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    public OapiV2UserGetResponse.UserGetResponse  getUserByUserId(String userId) {
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        req.setLanguage(Constants.ZH_CN);
        try {
            SnowDingTalkClient snowDingTalkRequest=new SnowDingTalkDefaultClient();
            OapiV2UserGetResponse response =snowDingTalkRequest.execute(req,BaseConstantUrl.GET_USER_BY_ID);
            if(response.getErrcode()==0){
                OapiV2UserGetResponse.UserGetResponse result = response.getResult();
                log.info("dingTalk????????????id?????????????????????????????????{}",result);
                return result;
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("dingTalk????????????id???????????????????????????????????????{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    public OapiV2UserListResponse.PageResult getUserInfoByDept(UserListRequest userListRequest) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_USERINFO_BY_DEPT);
        OapiV2UserListRequest req = new OapiV2UserListRequest();
        req.setDeptId(userListRequest.getDeptId());
        req.setCursor(userListRequest.getCursor());
        req.setSize(userListRequest.getSize());
        req.setOrderField("modify_desc");
        req.setContainAccessLimit(false);
        req.setLanguage(Constants.ZH_CN);
        try {
            OapiV2UserListResponse response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()==0){
                OapiV2UserListResponse.PageResult result = response.getResult();
                log.info("dingTalk?????????????????????????????????????????????{}",result);
                return result;
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("dingTalk???????????????????????????????????????????????????{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }


    @Override
    public OapiUserGetbyunionidResponse.UserGetByUnionIdResponse  getUserByUnionId(String unionId){

        DingTalkClient clientDingTalkClient = new DefaultDingTalkClient(BaseConstantUrl.GET_USER_UNION_ID);
        OapiUserGetbyunionidRequest request = new OapiUserGetbyunionidRequest();
        request.setUnionid(unionId);
        try {
            OapiUserGetbyunionidResponse  response = clientDingTalkClient.execute(request,getDingTalkToken());
            if(response.getErrcode()==0){
                OapiUserGetbyunionidResponse.UserGetByUnionIdResponse result = response.getResult();
                log.info("@@getUserByUnionId?????????????????????????????????{}",result);
                return result;
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }

        } catch (ApiException e) {
            log.error("getUserByUnionId???????????????????????????????????????{}",e.getErrMsg());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    public String getUnionIdBySysUserId(Long sysUserId) {
        return getUserByUserId(sysUserService.selectUserById(sysUserId).getDingUserId()).getUnionid();
    }

}
