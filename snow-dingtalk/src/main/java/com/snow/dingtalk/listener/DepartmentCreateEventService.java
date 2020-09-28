package com.snow.dingtalk.listener;

import com.snow.dingtalk.model.DepartmentCreateRequest;
import com.snow.dingtalk.service.DepartmentService;
import com.snow.system.domain.SysDept;
import com.snow.system.event.SyncEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qimingjin
 * @Title:  创建部门数据同步
 * @Description:
 * @date 2020/9/28 9:33
 */
public class DepartmentCreateEventService implements ISyncDingTalkInfo {

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void syncDingTalkInfoEvent(SyncEvent syncEvent) {
        SysDept sysDept=(SysDept)syncEvent.getT();
        DepartmentCreateRequest departmentDTO = DepartmentCreateRequest.builder().name(sysDept.getDeptName()).order(sysDept.getOrderNum())
                .parentid(sysDept.getParentName()).build();
        departmentService.createDepartment(departmentDTO);
    }
}
