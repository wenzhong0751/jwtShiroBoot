package com.shadow.springboot.application.shiro.provider.impl;

import com.shadow.springboot.application.domain.vo.Account;
import com.shadow.springboot.application.service.AccountService;
import com.shadow.springboot.application.shiro.provider.AccountProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author tomsun28
 * @date 9:22 2018/2/13
 */
@Service("accountProviderImpl")
public class AccountProviderImpl implements AccountProvider {

    @Autowired
    @Qualifier("accountServiceImpl")
    private AccountService accountService;

    @Override
    public Account loadAccount(String appId) {
        return accountService.loadAccount(appId);
    }
}
