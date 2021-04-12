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
 * @create: 2021-04-11 17:23
 **/
@Slf4j
@Service
public class CustomerStartListener extends AbstractExecutionListener<SysOaCustomerForm> {

    @Autowired
    private SysOaCustomerServiceImpl sysOaCustomerService;

    @Override
    protected void process() {

      //  Object variable = getVariable(SysOaResignForm.TRANSITION_PERSON);
        //开始的节点是获取不到表单的，因为这个时候流程id是生成了但是由于底层事务的关系，还没有落库。
        String businessKey= getBusinessKey();
        SysOaCustomer sysOaCustomer=new SysOaCustomer();
        sysOaCustomer.setCustomerStatus("ADMIT_ING");
        sysOaCustomer.setCustomerNo(businessKey);
        setVariable("customerManager",1);
        sysOaCustomerService.updateSysOaCustomerByCustomerNo(sysOaCustomer);
    }
}
