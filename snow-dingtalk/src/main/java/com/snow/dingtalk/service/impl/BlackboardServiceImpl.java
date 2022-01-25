package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiBlackboardCreateRequest;
import com.dingtalk.api.request.OapiBlackboardDeleteRequest;
import com.dingtalk.api.request.OapiBlackboardUpdateRequest;
import com.dingtalk.api.response.OapiBlackboardCreateResponse;
import com.dingtalk.api.response.OapiBlackboardDeleteResponse;
import com.dingtalk.api.response.OapiBlackboardUpdateResponse;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.service.BlackboardService;
import com.snow.system.domain.SysNotice;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysUserServiceImpl;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-14 15:18
 **/
@Service
@Slf4j
public class BlackboardServiceImpl extends BaseService implements BlackboardService {

    private SysUserServiceImpl sysUserService=SpringUtils.getBean(SysUserServiceImpl.class);


    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.BLACKBOARD_CREATE,dingTalkUrl=BaseConstantUrl.BLACKBOARD_CREATE)
    public String createBlackboard(SysNotice sysNotice) {

        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.BLACKBOARD_CREATE);
        OapiBlackboardCreateRequest req = new OapiBlackboardCreateRequest();
        OapiBlackboardCreateRequest.OapiCreateBlackboardVo boardVoObj = new OapiBlackboardCreateRequest.OapiCreateBlackboardVo();
        //设置公告管理员（先写死）
        boardVoObj.setOperationUserid("manager4480");
        //作者先写死
        boardVoObj.setAuthor("没用的阿吉");
        //保密等级：0：普通公告 20：保密公告
        boardVoObj.setPrivateLevel(0L);
        //是否发送应用内钉提醒
        boardVoObj.setDing(true);
        OapiBlackboardCreateRequest.BlackboardReceiverOpenVo receiverOpenVoObj = new OapiBlackboardCreateRequest.BlackboardReceiverOpenVo();
        receiverOpenVoObj.setUseridList(getUserDingId());
        boardVoObj.setBlackboardReceiver(receiverOpenVoObj);
        boardVoObj.setTitle(sysNotice.getNoticeTitle());
        boardVoObj.setPushTop(true);
        boardVoObj.setContent(sysNotice.getNoticeContent());
        req.setCreateRequest(boardVoObj);
        try {
            OapiBlackboardCreateResponse response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()!=0){
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
            return response.getRequestId();
        } catch (ApiException e) {
            log.error("新增公告deleteBlackboard异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.BLACKBOARD_DELETE,dingTalkUrl=BaseConstantUrl.DEPARTMENT_DELETE)
    public void deleteBlackboard(String blackboardId) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.BLACKBOARD_DELETE);
        OapiBlackboardDeleteRequest req = new OapiBlackboardDeleteRequest();
        req.setBlackboardId(blackboardId);
        req.setOperationUserid("manager4480");
        try {
            OapiBlackboardDeleteResponse response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()!=0){
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("删除公告deleteBlackboard异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.BLACKBOARD_UPDATE,dingTalkUrl=BaseConstantUrl.BLACKBOARD_UPDATE)
    public void updateBlackboard(SysNotice sysNotice) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.BLACKBOARD_UPDATE);
        OapiBlackboardUpdateRequest req = new OapiBlackboardUpdateRequest();
        OapiBlackboardUpdateRequest.OapiUpdateBlackboardVo boardVoObj = new OapiBlackboardUpdateRequest.OapiUpdateBlackboardVo();
        boardVoObj.setAuthor("没用的阿吉");
        boardVoObj.setDing(true);
        boardVoObj.setBlackboardId("");
        boardVoObj.setTitle(sysNotice.getNoticeTitle());
        boardVoObj.setContent(sysNotice.getNoticeContent());
        //修改后是否再次通知接收人
        boardVoObj.setNotify(true);
        //操作人userid，必须是公告管理员。
        boardVoObj.setOperationUserid("manager4480");
        req.setUpdateRequest(boardVoObj);
        try {
            OapiBlackboardUpdateResponse response = client.execute(req, getDingTalkToken());
            if(response.getErrcode()!=0){
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("修改公告deleteBlackboard异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }


    /**
     * 获取系统用户的dingId
     * @return
     */
    private List<String> getUserDingId(){
        SysUser sysUser=new SysUser();
        sysUser.setStatus("0");
        List<SysUser> userList = sysUserService.selectUserList(sysUser);
        return userList.stream().map(SysUser::getDingUserId).collect(Collectors.toList());
    }
}
