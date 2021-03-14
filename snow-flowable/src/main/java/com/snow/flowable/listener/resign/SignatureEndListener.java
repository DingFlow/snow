package com.snow.flowable.listener.resign;

import com.snow.common.enums.ProcessStatus;
import com.snow.flowable.domain.resign.SysOaResignForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import com.snow.system.domain.SysOaResign;
import com.snow.system.service.impl.SysOaResignServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description 离职申请结束监听器
 * @author: 没用的阿吉
 * @create: 2021-03-11 14:05
 **/
@Slf4j
@Service
public class SignatureEndListener  extends AbstractExecutionListener<SysOaResignForm> {

    @Autowired
    private SysOaResignServiceImpl sysOaResignService;

    @Override
    protected void process() {
        String businessKey= getBusinessKey();
        SysOaResign sysOaResign=new SysOaResign();
        sysOaResign.setProcessStatus(ProcessStatus.PASS.ordinal());
        sysOaResign.setResignNo(businessKey);
        sysOaResignService.updateSysOaResignByResignNo(sysOaResign);
    }
}
