package com.snow.flowable.listener.customer;

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
 * @create: 2021-04-11 17:37
 **/
@Service("customerErrorEndListener")
@Slf4j
public class CustomerErrorEndListener extends AbstractExecutionListener<SysOaCustomerForm> {

    @Autowired
    private SysOaCustomerServiceImpl sysOaCustomerService;

    @Override
    protected void process() {
        String businessKey= getBusinessKey();
        SysOaCustomer sysOaCustomer=new SysOaCustomer();
        sysOaCustomer.setCustomerNo(businessKey);
        sysOaCustomer.setCustomerStatus("BLACKLIST");
        sysOaCustomerService.updateSysOaCustomerByCustomerNo(sysOaCustomer);
    }
}
