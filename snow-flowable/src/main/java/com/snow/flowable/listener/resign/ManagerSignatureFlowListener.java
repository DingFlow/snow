package com.snow.flowable.listener.resign;

import com.snow.flowable.domain.resign.SysOaResignForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-10 23:00
 **/
@Service
@Slf4j
public class ManagerSignatureFlowListener extends AbstractExecutionListener<SysOaResignForm> {
    @Override
    protected void process() {
        SysOaResignForm appForms = getAppForms();
        //设置会签参数
    }
}
