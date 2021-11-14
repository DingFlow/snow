package com.snow.common.utils.formula;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 表达式计算引擎
 */
@Slf4j
public class MathFormula {

    /**
     * 通过对象执行公式
     * @param mathformula
     * @param o
     * @return
     */
    public static BigDecimal executeFormula(String mathformula, Object o) {
        MathEnv mathEnv = new MathEnv();
        Map<String, Object> env = mathEnv.getEnv(o);
        return compileAndExecute(mathformula, env);
    }

    /**
     * 通过Map执行公式
     * @param mathFormula
     * @param env
     * @return
     */
    private static BigDecimal compileAndExecute(String mathFormula, Map<String, Object> env) {
        // 打印计算公式
        log.info("**************************************************");
        log.info("@@传入的计算公式 => {}", mathFormula);
        Expression compiledExp = AviatorEvaluator.compile(mathFormula);
        // 打印变量和值
        env.entrySet().forEach(e ->log.info("{} => {}", e.getKey(), e.getValue()));
        // 打印结果
        BigDecimal result = (BigDecimal) compiledExp.execute(env);
        log.info("@@根据公式计算结果 => {}", result);
        log.info("**************************************************");
        return result;
    }

}
