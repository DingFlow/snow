package com.snow.common.annotation;

import java.lang.annotation.*;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/9/30 10:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})
@Documented
public @interface FormulaChinese {
    /**
     * 计算汉化名称
     * @return
     */
    String name() default "";
}
