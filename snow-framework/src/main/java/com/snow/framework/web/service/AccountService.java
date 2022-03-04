package com.snow.framework.web.service;

import com.snow.system.domain.SysFnAccount;
import com.snow.system.service.impl.SysFnAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/3/3 11:00
 */
@Service("account")
public class AccountService {

    @Autowired
    private SysFnAccountServiceImpl sysFnAccountService;

    public List<SysFnAccount> getAccounts(String accountNo){
        SysFnAccount sysFnAccount=new SysFnAccount();
        sysFnAccount.setAccountNo(accountNo);
        return sysFnAccountService.selectSysFnAccountList(sysFnAccount);
    }
}
