package com.snow.flowable.listener.customer;

import com.snow.common.utils.StringUtils;
import com.snow.flowable.domain.customer.SysOaCustomerForm;
import com.snow.flowable.listener.AbstractExecutionListener;
import com.snow.system.domain.SysDept;
import com.snow.system.domain.SysOaCustomer;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysDeptServiceImpl;
import com.snow.system.service.impl.SysOaCustomerServiceImpl;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private SysDeptServiceImpl sysDeptService;

    @Resource
    private SysUserServiceImpl sysUserService;

    @Override
    protected void process() {

      //  Object variable = getVariable(SysOaResignForm.TRANSITION_PERSON);
        //开始的节点是获取不到表单的，因为这个时候流程id是生成了但是由于底层事务的关系，还没有落库。
        //上级分配客户
        SysUser sysUser = getStartUserInfo();
        if(StringUtils.isNull(sysUser)){
            setVariable("customerManager",1);
        }else {
            SysDept sysDept = sysDeptService.selectDeptById(sysUser.getDeptId());
            Long parentId = sysDept.getParentId();
            SysUser newSysUser=new SysUser();
            newSysUser.setDeptId(parentId);
            List<SysUser> sysUsers = sysUserService.selectUserList(newSysUser);
            if(CollectionUtils.isEmpty(sysUsers)){
                setVariable("customerManager",1);
            }else {
                setVariable("customerManager",sysUsers.get(0).getUserId());
            }
        }
        String businessKey= getBusinessKey();
        SysOaCustomer sysOaCustomer=new SysOaCustomer();
        sysOaCustomer.setCustomerStatus("ADMIT_ING");
        sysOaCustomer.setCustomerNo(businessKey);
        sysOaCustomerService.updateSysOaCustomerByCustomerNo(sysOaCustomer);
    }
}
