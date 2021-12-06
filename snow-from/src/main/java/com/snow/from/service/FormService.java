package com.snow.from.service;

import com.snow.flowable.domain.response.ProcessDefinitionResponse;
import com.snow.flowable.service.FlowableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/12/3 14:39
 */
@Service("form")
public class FormService {

    @Autowired
    private FlowableService flowableService;

    public List<ProcessDefinitionResponse> getFlow(String key) {
        return flowableService.getProcessDefByKey(key);
    }
}
