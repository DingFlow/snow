package com.snow.dingtalk.listener;

import com.alibaba.fastjson.JSON;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.model.request.DepartmentCreateRequest;
import com.snow.dingtalk.service.impl.DepartmentServiceImpl;
import com.snow.system.domain.SysDept;
import com.snow.system.event.SyncEvent;
import com.snow.system.mapper.SysDeptMapper;
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

    private SysDeptMapper sysDeptMapper=SpringUtils.getBean("sysDeptMapper");

    @Override
    public void syncDingTalkInfoEvent(SyncEvent syncEvent) {
        log.info("@@调用钉钉部门传入的原始参数:{}"+JSON.toJSONString(syncEvent));
        DingTalkListenerType eventType =(DingTalkListenerType) syncEvent.getT();
        Integer code = eventType.getCode();
        if(code.equals(DingTalkListenerType.DEPARTMENT_CREATE.getCode())){
            SysDept sysDept=(SysDept)syncEvent.getSource();
            DepartmentCreateRequest departmentDTO = DepartmentCreateRequest.builder()
                    .name(sysDept.getDeptName())
                    .order(sysDept.getOrderNum())
                    .parentid(String.valueOf(sysDept.getParentId()))
                    .sourceIdentifier(sysDept.getDeptName())
                    .build();
            Long department = departmentService.createDepartment(departmentDTO);
            //添加钉钉部门成功后，反过来修改系统部门ID,保证系统部门id和钉钉部门id一致
            SysDept newSysDept=new SysDept();
            newSysDept.setNewDeptId(department);
            newSysDept.setDeptId(sysDept.getDeptId());
            sysDeptMapper.updateDept(newSysDept);
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
