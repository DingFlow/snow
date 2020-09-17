package com.snow.dingtalk;

import com.alibaba.fastjson.JSON;
import com.snow.dingtalk.model.DepartmentDTO;
import com.snow.dingtalk.service.DepartmentService;
import com.snow.system.domain.SysDept;
import com.snow.system.event.SyncEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/17 17:40
 */
@Component
public class SyncEventListener implements ApplicationListener<SyncEvent> {

    @Autowired
    private DepartmentService departmentService;
    @Override
    public void onApplicationEvent(SyncEvent syncEvent) {
        Integer eventType = syncEvent.getEventType();
        SysDept sysDept=(SysDept)syncEvent.getT();
        DepartmentDTO departmentDTO = DepartmentDTO.builder().name(sysDept.getDeptName()).order(sysDept.getOrderNum())
                .parentid(sysDept.getParentName()).build();
        departmentService.createDepartment(departmentDTO);
        System.out.println("监听到的事件类型:"+eventType+JSON.toJSONString(syncEvent));
    }
}
