package com.shadow.springboot.application.service;

import com.shadow.springboot.application.domain.bo.Permission;
import com.shadow.springboot.application.domain.bo.Resource;
import com.shadow.springboot.application.domain.bo.Role;
import com.shadow.springboot.application.domain.bo.User;
import org.springframework.data.domain.Page;

public interface RoleService {
    Page<Role> getPage(int pageNum, int pageSize);
    Boolean addRole(Role role);
    Boolean updateRole(Role role);
    Boolean deleteRoleByRoleId(String rid);

    Boolean authorityRoleResource(String rid,Integer pid);
    Boolean deleteAuthorityRoleResource(String rid,Integer pid);

    Page<Resource> getAuthorityApisByRoleId(String rid, int pageNum, int pageSize);
    Page<Resource> getNotAuthorityApisByRoleId(String rid, int pageNum, int pageSize);
    Page<Resource> getAuthorityMenusByRoleId(String rid, int pageNum, int pageSize);
    Page<Resource> getNotAuthorityMenusByRoleId(String rid, int pageNum, int pageSize);

    Page<User> getUserListByRoleId(String rid, int pageNum, int pageSize);
    Page<User> getNotAuthorityUserListByRoleId(String rid, int pageNum, int pageSize);
}
