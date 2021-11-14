package com.snow.flowable.listener.resign;

import com.snow.flowable.domain.resign.SysOaResignForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: snow
 * @description  交接人同意执行监听器
 * @author: 没用的阿吉
 * @create: 2021-03-10 23:00
 **/
@Service
@Slf4j
public class TransitionPersonSignatureFlowListener extends AbstractExecutionListener<SysOaResignForm> {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    protected void process() {
        SysOaResignForm appForms = getAppForms();
        //nrOfCompletedInstances/nrOfInstances > 0.50

        //设置会签参数
        //设置三个人作为多实例的人员
        List<String> userList = new ArrayList<>();
        userList.add("1");
        userList.add("2");
        userList.add("102");
        setVariable("hrSignatureList", userList);

    }
}
