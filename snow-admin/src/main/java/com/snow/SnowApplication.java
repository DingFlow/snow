package com.snow;

import com.snow.flowable.common.SpringContextUtil;
import org.flowable.ui.common.rest.idm.remote.RemoteAccountResource;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

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
        }
)
@ComponentScan(basePackages = {"com.snow",
        "org.flowable.ui",
        "org.jeecg.modules.jmreport"
}, excludeFilters= @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {RemoteAccountResource.class}))
@EnableAsync
public class SnowApplication extends AsyncConfigurerSupport {

    public static void main(String[] args) {
        ApplicationContext context=SpringApplication.run(SnowApplication.class, args);
        SpringContextUtil.setApplicationContext(context);
    }

    @Bean
    public FlowableModelerAppProperties flowableModelerAppProperties() {
        FlowableModelerAppProperties flowableModelerAppProperties=new FlowableModelerAppProperties();
        return flowableModelerAppProperties;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("DingFlowThread-");
        executor.initialize();
        return executor;
    }
}