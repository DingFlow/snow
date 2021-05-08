package com.snow.dingtalk.listener;

import com.alibaba.fastjson.JSON;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.model.DepartmentCreateRequest;
import com.snow.dingtalk.service.impl.DepartmentServiceImpl;
import com.snow.system.domain.SysDept;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qimingjin
 * @Title:  创建部门数据同步
 * @Description:
 * @date 2020/9/28 9:33
 */
@Slf4j
public class DepartmentEventService implements ISyncDingTalkInfo {


    private DepartmentServiceImpl departmentService=SpringUtils.getBean("departmentServiceImpl");


    @Override
    public void syncDingTalkInfoEvent(SyncEvent syncEvent) {
        log.info("调用创建钉钉部门传入的原始参数:{}"+JSON.toJSONString(syncEvent));
        DingTalkListenerType eventType =(DingTalkListenerType) syncEvent.getT();
        Integer code = eventType.getCode();
        if(code.equals(DingTalkListenerType.DEPARTMENT_CREATE.getCode())){
            SysDept sysDept=(SysDept)syncEvent.getSource();
            DepartmentCreateRequest departmentDTO = DepartmentCreateRequest.builder()
                    .name(sysDept.getDeptName())
                    .order(sysDept.getOrderNum())
                    .parentid(String.valueOf(sysDept.getParentId()))
                    .build();
            departmentService.createDepartment(departmentDTO);
        }else if(code.equals(DingTalkListenerType.DEPARTMENT_UPDATE.getCode())){
            SysDept sysDept=(SysDept)syncEvent.getSource();
            departmentService.updateDepartment(sysDept);
        }
        else if( code.equals(DingTalkListenerType.DEPARTMENT_DELETED.getCode())){
            Long id=(Long)syncEvent.getSource();
            departmentService.deleteDepartment(id);
        }

    }
}
