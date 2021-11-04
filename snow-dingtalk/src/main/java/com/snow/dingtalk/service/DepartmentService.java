package com.snow.dingtalk.service;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.snow.dingtalk.model.request.DepartmentCreateRequest;
import com.snow.system.domain.SysDept;
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
     * 更新部门
     * @param sysDept
     */
    String updateDepartment(SysDept sysDept);
    /**
     * 删除部门
     * @param
     */
    String deleteDepartment(Long id);

    /**
     * 根据ID获取钉钉部门详情
     * @param id
     */
    OapiV2DepartmentGetResponse.DeptGetResponse getDepartmentDetail(long id);

    /**
     * 获取部门详情
     * @return
     */
    List<OapiDepartmentListResponse.Department> getDingTalkDepartmentList();
}
