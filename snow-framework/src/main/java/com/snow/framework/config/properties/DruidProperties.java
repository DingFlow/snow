package com.snow.framework.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * druid 配置属性
 * privateKey:MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAjl7LDG9yssljrk70yXzSIHNZhYNiWHf3oV40DPSHhRzoKWWZ4A6tl8ZU3hFyOLMn/XuCTBPQoLaQ9hHJUTjyQwIDAQABAkAtwshPGe9rChJRL628qbM8nPb0VDOjLgRZSNdAJsT8gsA2BW9OasAHRIQfxWuiE0tlCg6UeMftQoltLPG3bCFJAiEAyNpvrKad/XQJGKz1jIhFQpiQ2Q9+U46qyZvTcj2bgf0CIQC1dbVRbS+2pCPDmHNFeNSw/8mGXRnBZLRB9D10TL9ZPwIhAKxk6LNc84BG50PZuIzdreziPHlCViBr9OVErXGBtYcVAiEAikytJeM+00fkjiW57T/7cu9wi7yXbaMwE3hwAhygVgcCIQCUs2A67Q3tYAVT8zRR0VM3APjeBGUVQI0ghXvFSnDe3w==
 * publicKey:MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI5eywxvcrLJY65O9Ml80iBzWYWDYlh396FeNAz0h4Uc6CllmeAOrZfGVN4RcjizJ/17gkwT0KC2kPYRyVE48kMCAwEAAQ==
 * password:VXTvn41xh0+92G8KAdvjQct2ZZ+IhRGm2MqTA38kayy48QwO/vSbTnTeTMVQ6s3axHWhQi9l1ZD5dyJUOenyNA==
 * @author snow
 */
@Configuration
public class DruidProperties
{
    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    private int maxEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.druid.connectProperties}")
    private String connectProperties;

    public DruidDataSource dataSource(DruidDataSource datasource)
    {
        /** 配置初始化大小、最小、最大 */
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        /** 配置获取连接等待超时的时间 */
        datasource.setMaxWait(maxWait);

        /** 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        /** 配置一个连接在池中最小、最大生存的时间，单位是毫秒 */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

        /**
         * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
         */
        datasource.setValidationQuery(validationQuery);
        /** 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
        datasource.setTestWhileIdle(testWhileIdle);
        /** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnBorrow(testOnBorrow);
        /** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnReturn(testOnReturn);
        /** 为数据库密码提供加密功能 */
        datasource.setConnectionProperties(connectProperties);
        return datasource;
    }
}
