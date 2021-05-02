package com.snow.flowable.listener;

import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.domain.AppForm;
import com.snow.flowable.service.FlowableService;

import com.snow.flowable.service.impl.AppFormServiceImpl;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @program: snow
 * @description 执行监听器
 * @author: 没用的阿吉
 * @create: 2020-12-05 16:59
 **/
@Slf4j
public abstract class AbstractExecutionListener<T extends AppForm> implements ExecutionListener {


    private ThreadLocal<T> appFormLocal = new ThreadLocal<>();

    private ThreadLocal<DelegateExecution> delegateExecutionLocal = new ThreadLocal();

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private AppFormServiceImpl appFormService;

    @Override
    @Transactional
    public void notify(DelegateExecution delegateExecution) {
        try {
            delegateExecutionLocal.set(delegateExecution);
            execute();
        } finally {
            //移除threadLocal变量，保证数据唯一
            appFormLocal.remove();
            delegateExecutionLocal.remove();
        }
    }

    protected void execute() {
        try {
            process();
        } catch (RuntimeException e) {
            log.error("执行任务异常", e);
            throw e;
        }
    }

    /**
     * 抽象需要执行的程序类
     */
    protected abstract void process();

    protected DelegateExecution getExecution() {
        return delegateExecutionLocal.get();
    }

    /**
     * 设置流程节点的变量
     *
     * @param key   变量名
     * @param value 变量值
     */
    protected void setVariable(String key, Object value) {
        getExecution().setVariable(key, value);
    }

    /**
     * 设置节点本地变量
     *
     * @param key   变量名
     * @param value 变量值
     */
    protected void setVariableLocal(String key, Object value) {
        getExecution().setVariableLocal(key, value);
    }

    /**
     * 获取流程变量
     */
    @SuppressWarnings("unchecked")
    protected <V> V getVariable(String key) {
        return (V) getExecution().getVariable(key);
    }

    /**
     * 获取流程节点本地变量
     */
    protected <V> V getVariableLocal(String key) {
        return (V) getExecution().getVariableLocal(key);
    }

    /**
     * 流程对应的业务主键
     */
    protected String getBusinessKey() {
        return getExecution().getProcessInstanceBusinessKey();
    }

    /**
     * 流程实例ID
     */
    protected String getProcessInstanceId() {
        return getExecution().getProcessInstanceId();
    }

    /**
     * 获取流程开始用户
     *
     * @return
     */
    protected String getStartUserName() {
        String startUserId = getVariable(FlowConstants.START_USER_ID);
        log.info("获取到的开始人startUserId:{}", startUserId);
        return Optional.ofNullable(sysUserService.selectUserById(Long.parseLong(startUserId)).getUserName()).orElse("");
    }

    /**
     * 获取流程开始用户ID
     * @return
     */
    protected SysUser getStartUserInfo() {
        String startUserId = getVariable(FlowConstants.START_USER_ID);
        log.info("获取到的开始人startUserId:{}", startUserId);
        return sysUserService.selectUserById(Long.parseLong(startUserId));
    }
    /**
     * 获取表单数据
     *
     * @return
     */
    public T getAppForms() {
        T t = appFormLocal.get();
        if (t == null) {
            appFormLocal.set(appFormService.getAppFrom(getProcessInstanceId()));
            t = appFormLocal.get();
        }
        return t;
    }

}

   
