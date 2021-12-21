package com.snow.flowable.service.impl;

import com.snow.flowable.service.FlowableFormService;
import org.flowable.engine.FormService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/18 14:00
 */
public class FlowableFormServiceImpl implements FlowableFormService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService  runtimeService;

    @Autowired
    private FormService formService;

    @Override
    public ProcessInstance startProcessInstanceByForm(String appForm) {

        //formService.submitStartFormData()
      //  runtimeService.startProcessInstanceByKey()
        return null;
    }
}
