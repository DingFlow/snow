package com.snow.dingtalk.listener;

import com.alibaba.fastjson.JSON;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.dingtalk.model.DepartmentCreateRequest;
import com.snow.dingtalk.service.DepartmentService;
import com.snow.system.domain.SysDept;
import com.snow.system.event.SyncEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @Title: 同步事件监听器
 * @Description:
 * @date 2020/9/17 17:40
 */
@Component
@Slf4j
public class SyncEventListener implements ApplicationListener<SyncEvent> {

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void onApplicationEvent(SyncEvent syncEvent) {
        log.info("进入监听器.....");
        Integer eventType = syncEvent.getEventType();
        if(eventType==DingTalkListenerType.DEPARTMENT_CREATE.getCode()){
            SysDept sysDept=(SysDept)syncEvent.getT();
            DepartmentCreateRequest departmentDTO = DepartmentCreateRequest.builder().name(sysDept.getDeptName()).order(sysDept.getOrderNum())
                    .parentid(sysDept.getParentName()).build();
            departmentService.createDepartment(departmentDTO);
        }
        else if(eventType == DingTalkListenerType.USER_CREATED.getCode()){

        }

        log.info("监听到的事件类型:"+eventType+JSON.toJSONString(syncEvent));
    }
}
