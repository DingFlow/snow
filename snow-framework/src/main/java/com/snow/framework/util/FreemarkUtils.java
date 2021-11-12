package com.snow.framework.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class FreemarkUtils {

    public static String process(String templateName,String templateValue,Map<String,Object> model){
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");
        StringWriter stringWriter = new StringWriter();
        Template template = null;
        try {
            template = new Template(templateName, templateValue, configuration);
            template.process(model, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            log.info("@@构建模板消息错误",e);
            throw  new RuntimeException(e);
        }
    }
}
