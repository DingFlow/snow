package com.snow.common.utils;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-01 17:03
 **/
public class PatternUtils {


    /**
     * 替换消息模板参数
     * @param map  后面改成freemark模板取值的方式
     * @param templateBody
     * @return
     */
    @Deprecated
    public static String builderTemplateBody(Map<String,Object> map, String templateBody){
        String re = "(?<=\\$\\{).*?(?=\\})";
        Pattern p = Pattern.compile(re);
        Matcher m = p.matcher(templateBody);
        String message = templateBody;
        while (m.find()) {
            String key = m.group();
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            //如果为空这取null标识
            Object value = Optional.ofNullable(map.get(key)).orElse("");
            message = message.replaceAll("\\$\\{" + key + "\\}",  String.valueOf(value));
        }
        return message;
    }
}
