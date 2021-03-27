package com.snow.framework.config;

import com.snow.common.constant.Constants;
import com.snow.framework.storage.AliyunStorage;
import com.snow.framework.storage.LocalStorage;
import com.snow.framework.storage.StorageService;
import com.snow.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qimingjin
 * @Title: 文件上传配置类
 * @Description:
 * @date 2021/1/11 11:22
 */
@Configuration
public class StorageAutoConfiguration {

    @Autowired
    private ISysConfigService configService;

    @Value("${sys.file.active}")
    private String active;

    @Bean
    public StorageService storageService() {
        StorageService storageService = new StorageService();
        storageService.setActive(active);
        if (active.equals("local")) {
            storageService.setStorage(localStorage());
        } else if (active.equals("aliyun")) {
            storageService.setStorage(aliyunStorage());
        } else {
            throw new RuntimeException("当前存储模式 " + active + " 不支持");
        }

        return storageService;
    }

    @Bean
    public LocalStorage localStorage() {
        LocalStorage localStorage = new LocalStorage();
        String address= configService.selectConfigByKey(Constants.LOCAL_ADDRESS);
        localStorage.setAddress(address);
        return localStorage;
    }

    @Bean
    public AliyunStorage aliyunStorage() {
        AliyunStorage aliyunStorage = new AliyunStorage();
        String accessKeyId= configService.selectConfigByKey(Constants.ALIYUN_ACCESSKEYID);
        String accessKeySecret= configService.selectConfigByKey(Constants.ALIYUN_ACCESSKEYSECRET);
        String bucketName= configService.selectConfigByKey(Constants.ALIYUN_BUCKETNAME);
        String endpoint= configService.selectConfigByKey(Constants.ALIYUN_ENDPOINT);
        aliyunStorage.setAccessKeyId(accessKeyId);
        aliyunStorage.setAccessKeySecret(accessKeySecret);
        aliyunStorage.setBucketName(bucketName);
        aliyunStorage.setEndpoint(endpoint);
        return aliyunStorage;
    }

}
