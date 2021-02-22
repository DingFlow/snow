package com.snow.flowable.service.impl;

import com.snow.flowable.service.FlowableDmnService;
import org.flowable.dmn.api.DmnRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-01-16 09:12
 **/
@Service
public class FlowableDmnServiceImpl implements FlowableDmnService {

    @Autowired
    private DmnRuleService dmnRuleService;

    @Override
    public void executeDecision() {
        Map<String, Object> map = dmnRuleService.createExecuteDecisionBuilder()
                .decisionKey("")
               // .variables()
                .executeWithSingleResult();
    }
}
