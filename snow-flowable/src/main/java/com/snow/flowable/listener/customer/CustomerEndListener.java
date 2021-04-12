package com.snow.flowable.listener.customer;


import com.alibaba.fastjson.JSON;
import com.snow.flowable.domain.customer.SysOaCustomerForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import com.snow.system.domain.SysOaCustomer;
import com.snow.system.service.impl.SysOaCustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-04-11 17:36
 **/
@Service
@Slf4j
public class CustomerEndListener extends AbstractExecutionListener<SysOaCustomerForm> {

    @Autowired
    private SysOaCustomerServiceImpl sysOaCustomerService;

    @Override
    protected void process() {

        SysOaCustomerForm appForms = getAppForms();
        log.info("获取到的表单数据：{}",JSON.toJSONString(appForms));
        String businessKey= getBusinessKey();
        SysOaCustomer sysOaCustomer=new SysOaCustomer();
        sysOaCustomer.setCustomerNo(businessKey);
        sysOaCustomer.setCustomerStatus("ADMITED");
        sysOaCustomerService.updateSysOaCustomerByCustomerNo(sysOaCustomer);
    }
}
