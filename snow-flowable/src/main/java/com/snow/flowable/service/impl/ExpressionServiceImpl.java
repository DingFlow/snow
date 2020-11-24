package com.snow.flowable.service.impl;

import com.snow.flowable.service.ExpressionService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.impl.de.odysseus.el.misc.TypeConverter;
import org.flowable.common.engine.impl.de.odysseus.el.misc.TypeConverterImpl;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/24 10:09
 */
@Service
@Slf4j
public class ExpressionServiceImpl implements ExpressionService {

    @Autowired
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    @Autowired
    private RuntimeService runtimeService;

    //@Autowired
    private TypeConverter typeConverter;
    @Override
    public Object getValue(String processInstanceId, String exp) {
        Expression expression = processEngineConfiguration.getExpressionManager().createExpression(exp);
        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();
        return expression.getValue(executionEntity);
    }

    /*@Override
    public <T> T getValue(String processInstanceId, String exp, Class<T> clazz) {
        Object value = this.getValue(processInstanceId, exp);
        return typeConverter.convert(value, clazz);
    }*/
}
