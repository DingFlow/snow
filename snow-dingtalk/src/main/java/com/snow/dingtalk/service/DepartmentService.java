package com.snow.dingtalk.service;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.snow.dingtalk.model.DepartmentCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/17 17:02
 */
@Service
public interface DepartmentService {
    /**
     * 创建部门
     * @param departmentDTO
     * @return
     */
    Long createDepartment(DepartmentCreateRequest departmentDTO);

    /**
     * 获取部门详情
     * @return
     */
    List<OapiDepartmentListResponse.Department> getDingTalkDepartmentList();
}
