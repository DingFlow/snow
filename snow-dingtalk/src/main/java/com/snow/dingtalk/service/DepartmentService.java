package com.snow.dingtalk.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentCreateRequest;
import com.dingtalk.api.response.OapiDepartmentCreateResponse;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.DepartmentDTO;
import com.taobao.api.ApiException;
import com.taobao.api.Constants;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/17 17:02
 */
@Service
public class DepartmentService extends BaseService {
    /**
     * 创建部门
     * @param departmentDTO
     * @return
     */
    public Long createDepartment(DepartmentDTO departmentDTO){
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
                syncDingTalkErrorOperLog(BaseConstantUrl.DEPARTMENT_CREATE,response.getErrmsg(),"createDepartment");
            }
        } catch (ApiException e) {
            syncDingTalkErrorOperLog(BaseConstantUrl.DEPARTMENT_CREATE,e.getMessage(),"createDepartment");
            e.printStackTrace();
        }
        return null;
    }
}
