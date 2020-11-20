package com.snow.flowable;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.snow.JunitTestApplication;
import com.snow.flowable.domain.StartProcessDTO;
import com.snow.flowable.service.FlowableService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/20 16:05
 */
@Slf4j
public class FlowableServiceTests extends JunitTestApplication {
    @Autowired
    private FlowableService flowableService;

    @Test
    public void startProcessInstanceByKey(){
        StartProcessDTO startProcessDTO=new StartProcessDTO();
        startProcessDTO.setStartUserId("1");
        startProcessDTO.setProcessDefinitionKey("snow_leave");
        startProcessDTO.setBusinessKey("QJ000001");
        Map<String,Object> map=Maps.newHashMap();
        map.put("manager","2");
        startProcessDTO.setVariables(map);
        ProcessInstance processInstance = flowableService.startProcessInstanceByKey(startProcessDTO);
        log.info("发布结果:{}",JSON.toJSONString(processInstance));
    }
}
