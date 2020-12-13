package com.snow.flowable.service.impl;

import com.alibaba.fastjson.JSON;

import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.domain.AppForm;
import com.snow.flowable.service.AppFormService;
import com.snow.flowable.service.FlowableService;
import com.snow.system.domain.SysOaLeave;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/7 10:20
 */
@Service
@Slf4j
public class AppFormServiceImpl implements AppFormService {

    @Autowired
    private FlowableService flowableService;


    @Override
    public <A extends AppForm> A getAppFrom(String classInfoJson,String className) {
        return JSON.parseObject(classInfoJson, getFormClass(className));
    }

    @Override
    public <A extends AppForm> A getAppFrom(String classInfoJson,Class<A> className) {
        return JSON.parseObject(classInfoJson, className);
    }


    @Override
    public <A extends AppForm> A getAppFromBySerializable(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance=flowableService.getHistoricProcessInstanceById(processInstanceId);
        return getVariable(historicProcessInstance, FlowConstants.APP_FORM);

    }



    @Override
    public <A extends AppForm> A getAppFrom(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance=flowableService.getHistoricProcessInstanceById(processInstanceId);
        String classInfoJson = getVariable(historicProcessInstance, FlowConstants.BUS_VAR);
        String classPackName = getVariable(historicProcessInstance, FlowConstants.CLASS_PACK_NAME);
        return getAppFrom(classInfoJson, classPackName);
    }

    /**
     * 获取流程变量
     * @param key
     * @param <V>
     * @return
     */
    protected <V> V  getVariable(HistoricProcessInstance historicProcessInstance,String key){
        return (V)historicProcessInstance.getProcessVariables().get(key);
    }

    private <A> Class<A> getFormClass(String className) {
        try {
            return (Class<A>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
