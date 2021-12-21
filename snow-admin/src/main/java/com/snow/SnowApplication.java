package com.snow;

import com.snow.flowable.common.SpringContextUtil;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 启动程序
 *
 * @author snow
 */
@SpringBootApplication(
        exclude= {
                DataSourceAutoConfiguration.class,
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class,
                LiquibaseAutoConfiguration.class
        },
        scanBasePackages = {"com.snow","org.flowable.ui" }

)
public class SnowApplication
{
    public static void main(String[] args)
    {
        ApplicationContext context=SpringApplication.run(SnowApplication.class, args);
        SpringContextUtil.setApplicationContext(context);


    }
    @Bean
    public FlowableModelerAppProperties flowableModelerAppProperties() {
        FlowableModelerAppProperties flowableModelerAppProperties=new FlowableModelerAppProperties();
        return flowableModelerAppProperties;
    }
}