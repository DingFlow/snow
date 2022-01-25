package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.request.DepartmentCreateRequest;
import com.snow.dingtalk.service.DepartmentService;
import com.snow.system.domain.SysDept;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_CREATE,dingTalkUrl=BaseConstantUrl.DEPARTMENT_CREATE)
    public Long createDepartment(DepartmentCreateRequest departmentDTO){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DEPARTMENT_CREATE);
        OapiDepartmentCreateRequest request = new OapiDepartmentCreateRequest();
        request.setParentid(departmentDTO.getParentid());
        request.setCreateDeptGroup(departmentDTO.getCreateDeptGroup());
        request.setOrder(departmentDTO.getOrder());
        request.setName(departmentDTO.getName());
        request.setHttpMethod(com.taobao.api.Constants.METHOD_POST);
        request.setSourceIdentifier(departmentDTO.getSourceIdentifier());
        try {
            OapiDepartmentCreateResponse response = client.execute(request,getDingTalkToken());
            if(response.getErrcode()==0){
                return response.getId();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉创建部门createDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_UPDATE,dingTalkUrl=BaseConstantUrl.DEPARTMENT_UPDATE)
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
        req.setLanguage(Constants.ZH_CN);
        req.setAutoAddUser(false);
        //部门主管列表
        if(!CollectionUtils.isEmpty(sysDept.getLeader())){
            req.setDeptManagerUseridList(sysDept.getLeader().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }

        try {
            OapiV2DepartmentUpdateResponse rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()==0){
               return rsp.getRequestId();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@更新钉钉部门updateDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_DELETED,dingTalkUrl=BaseConstantUrl.DEPARTMENT_DELETE)
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
            log.error("@@删除钉钉部门deleteDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }

    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_QUERY,dingTalkUrl=BaseConstantUrl.GET_DEPARTMENT_BY_ID)
    public OapiV2DepartmentGetResponse.DeptGetResponse getDepartmentDetail(long id) {
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_DEPARTMENT_BY_ID);
        OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
        req.setDeptId(id);
        req.setLanguage(Constants.ZH_CN);
        try {
            OapiV2DepartmentGetResponse rsp = client.execute(req, getDingTalkToken());
            if(rsp.getErrcode()==0){
                return rsp.getResult();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            throw new SyncDataException(JSON.toJSONString(req),e.getMessage());
        }
    }


    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_QUERY,dingTalkUrl=BaseConstantUrl.DEPARTMENT_LIST)
    public List<OapiDepartmentListResponse.Department> getDingTalkDepartmentList(){
        DingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.DEPARTMENT_LIST);
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
       // request.setId("1");
        request.setHttpMethod("GET");
        request.setFetchChild(true);
        try {
            OapiDepartmentListResponse response = client.execute(request, getDingTalkToken());
            if(response.getErrcode()==0){
                return response.getDepartment();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            throw new SyncDataException(JSON.toJSONString(request),e.getMessage());
        }
    }

}
