package com.snow.flowable.common;



import org.flowable.common.engine.impl.de.odysseus.el.ExpressionFactoryImpl;
import org.flowable.common.engine.impl.de.odysseus.el.util.SimpleContext;
import org.flowable.common.engine.impl.javax.el.ExpressionFactory;
import org.flowable.common.engine.impl.javax.el.ValueExpression;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/24 14:21
 */
public class ConditionElUtil {
    private ConditionElUtil() {

    }

    public static Boolean checkFormDataByRuleEl(String el, Map<String, Object> formData) {

        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();

        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                context.setVariable(entry.getKey(), factory.createValueExpression(value, value.getClass()));
            }
        }
        ValueExpression e = factory.createValueExpression(context, el, Boolean.class);
        return (Boolean) e.getValue(context);

    }

    public static void main(String[] args) {
        String el = "${请假天数>3 && 部门 == '产品部'}";
        Map<String, Object> formData = new HashMap<>();
        formData.put("请假天数", 1);
        formData.put("部门", "产品部");
        System.out.println(ConditionElUtil.checkFormDataByRuleEl(el, formData));
    }
}