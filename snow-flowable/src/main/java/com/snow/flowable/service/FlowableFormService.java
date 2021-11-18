package com.snow.flowable.service;

import com.snow.flowable.domain.AppForm;
import org.flowable.engine.runtime.ProcessInstance;

/**
 * @author qimingjin
 * @Title: form 表单流程
 * @Description:
 * @date 2021/11/18 13:58
 */
public interface FlowableFormService {

    ProcessInstance startProcessInstanceByForm(String appForm);
}
