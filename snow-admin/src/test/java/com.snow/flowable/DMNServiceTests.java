package com.snow.flowable;

import com.google.common.collect.Maps;
import com.snow.JunitTestApplication;
import com.snow.common.json.JSON;
import lombok.extern.slf4j.Slf4j;
import org.flowable.dmn.api.*;
import org.flowable.dmn.engine.DmnEngineConfiguration;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/14 16:31
 */
@Slf4j
public class DMNServiceTests extends JunitTestApplication {

    @Autowired
    private DmnRuleService dmnRuleService;

    @Autowired
    private DmnEngineConfiguration dmnEngineConfiguration;

    @Autowired
    private DmnHistoryService dmnHistoryService;

    @Autowired
    private DmnManagementService dmnManagementService;

    @Test
    public void executeDecisionTest(){
        ExecuteDecisionBuilder variable = dmnRuleService.createExecuteDecisionBuilder().variable("", "");
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("manager","网二");
        paramMap.put("zjl","张三");
        paramMap.put("rs","李四");
        Map<String, Object> result = dmnRuleService.createExecuteDecisionBuilder()
                .decisionKey("testKey")
                .variables(paramMap)
                .variable("money", "50")
                .executeWithSingleResult();

        List<DmnHistoricDecisionExecution> testKey = dmnHistoryService.createHistoricDecisionExecutionQuery().decisionKey("testKey")
                .orderByEndTime()
                .desc()
                .list();
        log.info(com.alibaba.fastjson.JSON.toJSONString(testKey));
    }
}
