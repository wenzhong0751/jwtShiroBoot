package com.shadow.springboot.application.service;

import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.domain.vo.UserSearchVo;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface UserService {
    Page<User> getPage(int pageNum, int pageSize);
    Page<User> getPage(int pageNum, int pageSize, UserSearchVo searchVo);
    Set<String> getRoleSet(String username);
    Boolean authority(Long uid,String rid);
    Boolean authorityList(Long uid,String rids);
    Boolean deleteAuthority(Long uid,String rid);
    User getByUsername(String username);
    User save(User user);
    Boolean update(User user);
}
