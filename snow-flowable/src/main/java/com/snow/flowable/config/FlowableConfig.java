package com.snow.flowable.config;

import com.snow.flowable.listener.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.event.support.TypedEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.EngineDeployer;
import org.flowable.engine.impl.rules.RulesDeployer;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/18 19:16
 */
@Configuration
@Slf4j
public class FlowableConfig {


    @Primary
    @Bean(name = "processEngineConfiguration")
    public SpringProcessEngineConfiguration getSpringProcessEngineConfiguration(ApplicationContext applicationContext, DataSource dataSource, DataSourceTransactionManager transactionManager) {
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
        //具体事件的监听器
        Map<String, AbstractEventListener> beanListeners = applicationContext.getBeansOfType(AbstractEventListener.class);
        ArrayList<FlowableEventListener> flowableEventListeners = new ArrayList(beanListeners.values());
        flowableEventListeners.removeIf(eventListener -> eventListener instanceof TypedEventListener);
        configuration.setEventListeners(flowableEventListeners);
        //设置流程图显示乱码
        configuration.setActivityFontName("宋体");
        configuration.setLabelFontName("宋体");
        configuration.setAnnotationFontName("宋体");
        return configuration;
    }

}
