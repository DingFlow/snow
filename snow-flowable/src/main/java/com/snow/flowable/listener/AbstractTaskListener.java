package com.snow.flowable.listener;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.snow.common.constant.Constants;
import com.snow.common.utils.GenericUtil;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.domain.FinishTaskDTO;
import com.snow.flowable.service.FlowableService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/12/7 16:39
 */
@Slf4j
public abstract class AbstractTaskListener<T extends FinishTaskDTO> implements TaskListener {

    @Autowired
    private FlowableService flowableService;

    private final ThreadLocal<DelegateTask> delegateTaskThreadLocal = new ThreadLocal<>();

    private final ThreadLocal<T> localTaskParam = new ThreadLocal<>();

    @Override
    public void notify(DelegateTask delegateTask) {
        try {
            delegateTaskThreadLocal.set(delegateTask);
            String form = JSON.toJSONString(getTaskLocalParams());
            T param = JSON.parseObject(form, GenericUtil.getGenericTypeOf(AbstractTaskListener.class, 0, getClass()));
            localTaskParam.set(param);
            processTask();
            //保存修改后的变量
            updateTaskVars(param);
        } catch (Exception e){
            log.error("执行AbstractTaskListener监听异常:{}",e.getMessage());
            throw new RuntimeException("执行AbstractTaskListener监听异常！");
        }finally {
            delegateTaskThreadLocal.remove();
            localTaskParam.remove();
        }

    }

    protected abstract void processTask();

    /**
     * 设置流程节点的变量
     *
     * @param key   变量名
     * @param value 变量值
     */
    protected void setVariable(String key, Object value) {
        getDelegateTask().setVariable(key, value);
    }

    /**
     * 设置节点本地变量
     *
     * @param key   变量名
     * @param value 变量值
     */
    protected void setVariableLocal(String key, Object value) {
        getDelegateTask().setVariableLocal(key, value);
    }

    /**
     * 根据key获取节点本地变量
     *
     * @param key
     * @param <V>
     * @return
     */
    protected <V> V getVariableLocal(String key) {
        return (V) getDelegateTask().getVariableLocal(key);
    }

    /**
     * 获取流程变量
     *
     * @param key
     * @param <V>
     * @return
     */
    protected <V> V getVariable(String key) {
        return (V) getDelegateTask().getVariable(key);
    }

    protected <V> V getVariable(String key, Class<V> varClass) {
        return getDelegateTask().getVariable(key, varClass);
    }

    protected DelegateTask getDelegateTask() {
        return delegateTaskThreadLocal.get();
    }

    /**
     * 获取审批结果
     *
     * @return
     */
    protected Boolean getApprovalResult() {
        return getVariable(FlowConstants.IS_PASS);
    }

    /**
     * 流程实例ID
     */
    protected String getProcessInstanceId() {
        return getDelegateTask().getProcessInstanceId();
    }

    /**
     * 流程对应的业务主键
     */
    protected String getBusinessKey() {
        ProcessInstance processInstance = flowableService.getProcessInstanceById(getProcessInstanceId());
        return processInstance.getBusinessKey();
    }

    /**
     * 获取流程发起人
     *
     * @return
     */
    protected String getStartUserId() {
        ProcessInstance processInstance = flowableService.getProcessInstanceById(getProcessInstanceId());
        return processInstance.getStartUserId();
    }

    /**
     * 获取节点名称
     */
    protected String getTaskName() {
        return getDelegateTask().getName();
    }


    protected  T  getTaskParam(){
        return localTaskParam.get();
    }

    protected  Map<String, Object>  getTaskLocalParams(){
        return getDelegateTask().getVariablesLocal();
    }

    /**
     * 保存修改后的变量
     * @param param
     */
    protected void updateTaskVars(T param) {
        // 设置Execution级别的isPass变量，控制后续流转
        setVariable(FlowConstants.IS_PASS, param.getIsPass());
        // 记录当前节点提交的是否通过的变量
        setVariableLocal(FlowConstants.IS_PASS, param.getIsPass());
        // 最后操作者
        setVariable(FlowConstants.LAST_OPERATOR, param.getUserId());
        getDelegateTask().setVariablesLocal(BeanUtil.beanToMap(param));
    }

}
