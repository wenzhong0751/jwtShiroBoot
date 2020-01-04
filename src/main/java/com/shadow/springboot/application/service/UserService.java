package com.shadow.springboot.application.service;

import com.shadow.springboot.application.domain.bo.User;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface UserService {
    Page<User> getPage(int pageNum, int pageSize);
    Set<String> getRoleSet(String username);
    Boolean authority(Long uid,String rid);
    Boolean deleteAuthority(Long uid,String rid);
    User getByUsername(String username);
    User save(User user);
}
