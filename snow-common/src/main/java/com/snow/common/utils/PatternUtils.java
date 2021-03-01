package com.snow.common.utils;

import java.util.Map;
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
     * @param map
     * @param templateBody
     * @return
     */
    public static String builderTemplateBody(Map<String,String> map, String templateBody){
        // 正则匹配 ${xx}
        Pattern regex = Pattern.compile("\\$\\{(.*?)\\}");

        Matcher m = regex.matcher(templateBody);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String group = m.group(1);
            m.appendReplacement(sb, map.get(group));
        }
        m.appendTail(sb);

        return sb.toString();
    }
}
