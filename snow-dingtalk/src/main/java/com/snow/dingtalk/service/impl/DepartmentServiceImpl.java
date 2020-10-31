package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentCreateRequest;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.response.OapiDepartmentCreateResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.DepartmentCreateRequest;
import com.snow.dingtalk.service.DepartmentService;
import com.snow.system.service.impl.SysDeptServiceImpl;
import com.taobao.api.ApiException;
import com.taobao.api.Constants;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-10-31 12:59
 **/
@Service(value = "departmentServiceImpl")
public class DepartmentServiceImpl extends BaseService implements DepartmentService  {

    private SysDeptServiceImpl sysDeptServiceImpl=SpringUtils.getBean("sysDeptServiceImpl");

    @Override
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
                syncDingTalkErrorOperLog(BaseConstantUrl.DEPARTMENT_CREATE,response.getMessage(),"createDepartment",JSON.toJSONString(request));
                return response.getId();
            }else {
                syncDingTalkErrorOperLog(BaseConstantUrl.DEPARTMENT_CREATE,response.getErrmsg(),"createDepartment",JSON.toJSONString(request));
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.DEPARTMENT_CREATE,e.getMessage(),"createDepartment",JSON.toJSONString(request));
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
                syncDingTalkErrorOperLog(BaseConstantUrl.DEPARTMENT_LIST,response.getMessage(),"getDingTalkDepartmentList",JSON.toJSONString(request));
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


    public void sycnDepartmentData(){
        List<OapiDepartmentListResponse.Department> dingTalkDepartmentList = getDingTalkDepartmentList();


    }
}
