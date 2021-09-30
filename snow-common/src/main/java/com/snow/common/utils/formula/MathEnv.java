package com.snow.common.utils.formula;

import com.snow.common.annotation.FormulaChinese;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 表达式计算引擎
 */
@Slf4j
public class MathEnv {

    /**
     * 根据对象获取变量和值
     * @param o
     * @return
     */
    public Map<String, Object> getEnv(Object o) {
        Map<String, Object> env = new HashMap<>(30);
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field f : fields) {
            FormulaChinese annotation = f.getAnnotation(FormulaChinese.class);
            if (null != annotation) {
                String name = annotation.name();
                f.setAccessible(true);
                Object fieldVal = null;
                try {
                    fieldVal = f.get(o);
                } catch (IllegalAccessException e) {
                    log.info("exception:{}", e);
                    throw new RuntimeException(e);
                }
                env.put(name, fieldVal);
            }
        }
        return env;
    }
}
