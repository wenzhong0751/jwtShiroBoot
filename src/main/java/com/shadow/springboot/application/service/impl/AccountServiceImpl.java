package com.shadow.springboot.application.service.impl;

//import com.usthe.bootshiro.dao.AuthUserMapper;
//import com.usthe.bootshiro.domain.bo.AuthUser;
//import com.usthe.bootshiro.domain.vo.Account;
//import com.usthe.bootshiro.service.AccountService;
//import com.usthe.bootshiro.service.UserService;

import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.domain.vo.Account;
import com.shadow.springboot.application.repository.UserRepository;
import com.shadow.springboot.application.service.AccountService;
import com.shadow.springboot.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

/**
 * @author tomsun28
 * @date 22:04 2018/3/7
 */
@Service("accountServiceImpl")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepository userRepository;

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Override
    public Account loadAccount(String appId) {
        Optional<User> userOptional = userRepository.findByUsername(appId);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            return new Account(user.getUsername(), user.getPassword(), user.getSalt());
        }
        return null;
    }

    @Override
    public boolean isAccountExistByUid(String uid) {
        Optional<User> user = userRepository.findByUsername(uid);
        return user.isPresent() ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public boolean registerAccount(User account) {
        // 给新用户授权访客角色
        User user = userService.save(account);
        return userService.authority(user.getUid(), "role_guest");
    }

    @Override
    public String loadAccountRole(String appId) {
        Set<String> stringSet = userService.getRoleSet(appId);
        String roles = String.join(",",stringSet);
        return roles;
    }
}
