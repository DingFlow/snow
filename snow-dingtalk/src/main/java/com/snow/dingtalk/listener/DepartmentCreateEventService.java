package com.snow.dingtalk.listener;

import com.alibaba.fastjson.JSON;
import com.snow.dingtalk.model.DepartmentCreateRequest;
import com.snow.dingtalk.service.DepartmentService;
import com.snow.system.domain.SysDept;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qimingjin
 * @Title:  创建部门数据同步
 * @Description:
 * @date 2020/9/28 9:33
 */
@Slf4j
public class DepartmentCreateEventService implements ISyncDingTalkInfo {

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void syncDingTalkInfoEvent(SyncEvent syncEvent) {
        log.info("调用创建钉钉组织架构传入的原始参数:{}"+JSON.toJSONString(syncEvent));
        SysDept sysDept=(SysDept)syncEvent.getT();
        DepartmentCreateRequest departmentDTO = DepartmentCreateRequest.builder()
                .name(sysDept.getDeptName())
                .order(sysDept.getOrderNum())
                .parentid(sysDept.getParentName())
                .build();
        departmentService.createDepartment(departmentDTO);
    }
}
