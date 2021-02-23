package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.annotation.SyncLog;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.DepartmentCreateRequest;
import com.snow.dingtalk.service.DepartmentService;
import com.snow.system.domain.SysDept;
import com.snow.system.service.impl.SysDeptServiceImpl;
import com.taobao.api.ApiException;
import com.taobao.api.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-10-31 12:59
 **/
@Service(value = "departmentServiceImpl")
@Slf4j
public class DepartmentServiceImpl extends BaseService implements DepartmentService  {

    @Override
    @SyncLog(dingTalkListenerType = DingTalkListenerType.DEPARTMENT_CREATE,dingTalkUrl=BaseConstantUrl.DEPARTMENT_CREATE)
    public Long createDepartment(DepartmentCreateRequest departmentDTO){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DEPARTMENT_CREATE);
        OapiDepartmentCreateRequest request = new OapiDepartmentCreateRequest();
        request.setParentid(departmentDTO.getParentid());
        request.setCreateDeptGroup(departmentDTO.getCreateDeptGroup());
        request.setOrder(departmentDTO.getOrder());
        request.setName(departmentDTO.getName());
        request.setHttpMethod(Constants.METHOD_POST);
        request.setSourceIdentifier(departmentDTO.getSourceIdentifier());
        try {
            OapiDepartmentCreateResponse response = client.execute(request,getDingTalkToken());
            if(response.getErrcode()==0){
                return response.getId();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            log.error("钉钉创建部门createDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    @SyncLog(dingTalkListenerType = DingTalkListenerType.DEPARTMENT_UPDATE,dingTalkUrl=BaseConstantUrl.DEPARTMENT_UPDATE)
    public String updateDepartment(SysDept sysDept) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DEPARTMENT_UPDATE);
        OapiV2DepartmentUpdateRequest req = new OapiV2DepartmentUpdateRequest();
        req.setDeptId(sysDept.getDeptId());
        req.setParentId(sysDept.getParentId());
        //是否限制本部门成员查看通讯录：true：开启限制。开启后本部门成员只能看到限定范围内的通讯录 false：不限制
        req.setOuterDept(false);
        req.setHideDept(false);
        //是否创建群
        req.setCreateDeptGroup(true);
        req.setOrder(Long.parseLong(sysDept.getOrderNum()));
        req.setName(sysDept.getDeptName());
        req.setLanguage("zh_CN");
        req.setAutoAddUser(false);
        //部门主管列表
        req.setDeptManagerUseridList(sysDept.getLeader());
        try {
            OapiV2DepartmentUpdateResponse rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()==0){
               return rsp.getRequestId();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            log.error("更新钉钉部门updateDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @SyncLog(dingTalkListenerType = DingTalkListenerType.DEPARTMENT_DELETED,dingTalkUrl=BaseConstantUrl.DEPARTMENT_DELETE)
    public String deleteDepartment(Long id) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DEPARTMENT_DELETE);
        OapiV2DepartmentDeleteRequest req = new OapiV2DepartmentDeleteRequest();
        req.setDeptId(id);

        try {
            OapiV2DepartmentDeleteResponse rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()==0){
                return rsp.getRequestId();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            log.error("删除钉钉部门deleteDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }

    }

    @Override
    public OapiV2DepartmentGetResponse.DeptGetResponse getDepartmentDetail(long id) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_DEPARTMENT_BY_ID);
        OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
        req.setDeptId(id);
        req.setLanguage("zh_CN");
        try {
            OapiV2DepartmentGetResponse rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()==0){
                syncDingTalkSuccessOperLog(BaseConstantUrl.GET_DEPARTMENT_BY_ID,rsp.getMessage(),"getDepartmentDetail",JSON.toJSONString(req));
                return rsp.getResult();
            }else {
                syncDingTalkErrorOperLog(BaseConstantUrl.DEPARTMENT_LIST,rsp.getErrmsg(),"getDingTalkDepartmentList",JSON.toJSONString(req));
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.GET_DEPARTMENT_BY_ID,e.getMessage(),"getDepartmentDetail",JSON.toJSONString(req));
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<OapiDepartmentListResponse.Department> getDingTalkDepartmentList(){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DEPARTMENT_LIST);
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setId("1");
        request.setHttpMethod("GET");
        request.setFetchChild(true);
        try {
            OapiDepartmentListResponse response = client.execute(request, getDingTalkToken());
            if(response.getErrcode()==0){
                syncDingTalkSuccessOperLog(BaseConstantUrl.DEPARTMENT_LIST,response.getMessage(),"getDingTalkDepartmentList",JSON.toJSONString(request));
                return response.getDepartment();
            }else {
                syncDingTalkErrorOperLog(BaseConstantUrl.DEPARTMENT_LIST,response.getErrmsg(),"getDingTalkDepartmentList",JSON.toJSONString(request));
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.DEPARTMENT_LIST,e.getMessage(),"getDingTalkDepartmentList",JSON.toJSONString(request));
            e.printStackTrace();
        }
        return null;
    }


}
