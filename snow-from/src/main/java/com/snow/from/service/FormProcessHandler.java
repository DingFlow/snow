package com.snow.from.service;

import com.snow.common.core.domain.ProcessEventRequest;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.service.impl.SysFormDataRecordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/12/6 16:02
 */
@Component
@Slf4j
public class FormProcessHandler implements ApplicationListener<ProcessEventRequest> {

    @Autowired
    private SysFormDataRecordServiceImpl formDataRecordService;

    @Override
    public void onApplicationEvent(ProcessEventRequest processEventRequest) {
        log.info("@@流程状态变更监听器监听到的数据主键：{}", processEventRequest.getBusinessKey());
        SysFormDataRecord sysFormDataRecord=new SysFormDataRecord();
        sysFormDataRecord.setFormNo(processEventRequest.getBusinessKey());
        sysFormDataRecord.setFormStatus(processEventRequest.getProcessStatus());
        formDataRecordService.updateSysFormDataRecordByNo(sysFormDataRecord);
    }
}
