package com.snow.flowable.config;

import com.google.common.collect.Lists;
import com.snow.flowable.listener.AbstractEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.EngineDeployer;
import org.flowable.engine.impl.rules.RulesDeployer;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/18 19:16
 */
@Configuration
public class FlowableConfig{


    @Primary
    @Bean(name = "processEngineConfiguration")
    public SpringProcessEngineConfiguration getSpringProcessEngineConfiguration(DataSource dataSource, DataSourceTransactionManager transactionManager) {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setTransactionManager(transactionManager);
        configuration.setDatabaseSchemaUpdate("false");
        configuration.setAsyncExecutorActivate(true);
        configuration.setCustomPostDeployers(new ArrayList<EngineDeployer>(){
            private static final long serialVersionUID = 4041439225480991716L;
            {
                add(new RulesDeployer());
            }
        });
        //注入全局监听器
        List<FlowableEventListener> flowableEventListenerList=Lists.newArrayList();
        flowableEventListenerList.add(new AbstractEventListener());
        configuration.setEventListeners(flowableEventListenerList);
        //设置流程图显示乱码
        configuration.setActivityFontName("宋体");
        configuration.setLabelFontName("宋体");
        configuration.setAnnotationFontName("宋体");
        return configuration;
    }

}
