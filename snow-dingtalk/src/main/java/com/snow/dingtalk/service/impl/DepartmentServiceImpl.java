package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentCreateRequest;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.response.OapiDepartmentCreateResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.snow.common.annotation.DingTalkSyncLog;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.enums.DingTalkSyncType;
import com.snow.common.exception.DingTalkSyncException;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.DepartmentCreateRequest;
import com.snow.dingtalk.service.DepartmentService;
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

    private SysDeptServiceImpl sysDeptServiceImpl=SpringUtils.getBean("sysDeptServiceImpl");

    @Override
    @DingTalkSyncLog(dingTalkListenerType = DingTalkListenerType.DEPARTMENT_CREATE,dingTalkUrl=BaseConstantUrl.DEPARTMENT_CREATE)
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
                throw new DingTalkSyncException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            log.error("钉钉创建部门createDepartment异常：{}",e.getMessage());
            throw new DingTalkSyncException(JSON.toJSONString(request),e.getErrMsg());
        }
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
