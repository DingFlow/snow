package com.snow.flowable;

import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/18 19:16
 */
//@Configuration
public class ProcessEngineConfig {
    /**
     * ProcessEngine 配置，其中DataSourceTransactionManager和DataSource自动注入
     * @param dataSourceTransactionManager
     * @param dataSource
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSourceTransactionManager dataSourceTransactionManager, DataSource dataSource) {
        SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setDataSource(dataSource);
        springProcessEngineConfiguration.setTransactionManager(dataSourceTransactionManager);

        //不添加此项配置，在没创建表时，会抛出FlowableWrongDbException异常
        springProcessEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return springProcessEngineConfiguration;
    }
}
