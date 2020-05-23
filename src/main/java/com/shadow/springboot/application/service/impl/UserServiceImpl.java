package com.shadow.springboot.application.service.impl;

import com.shadow.springboot.application.domain.bo.Role;
import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.domain.vo.UserSearchVo;
import com.shadow.springboot.application.repository.RoleRepository;
import com.shadow.springboot.application.repository.UserRepository;
import com.shadow.springboot.application.service.UserService;
import com.shadow.springboot.application.util.BeanCopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<User> getPage(int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.ASC, "uid");
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> getPage(int pageNum, int pageSize, UserSearchVo searchVo) {
        Sort sort = new Sort(Sort.Direction.ASC, "uid");
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);

        Specification<User> specification = new Specification<User>(){
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<String> username = root.get("username");
                Predicate p1 = criteriaBuilder.like(username,"%" + searchVo.getUsername() + "%");
                return p1;
            }
        };
        return userRepository.findAll(specification,pageable);
    }

    @Override
    public Set<String> getRoleSet(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Set<String> roleSet = new HashSet<String>();
        if (user.isPresent()){
            List<Role> roles = user.get().getRoleList();
            for (Role role : roles) {
                roleSet.add(role.getRid());
            }
        }
        return roleSet;
    }

    @Override
    public Boolean authority(Long uid, String rid) {
        Optional<User> user = userRepository.findById(uid);
        if (user.isPresent()) {
            Optional<Role> role = roleRepository.findById(rid);
            if (role.isPresent()) {
                user.get().getRoleList().add(role.get());
                userRepository.save(user.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean deleteAuthority(Long uid, String rid) {
        Optional<User> user = userRepository.findById(uid);
        if (user.isPresent()) {
            List<Role> roleList = user.get().getRoleList();
            for (int i = 0; i < roleList.size(); i++) {
                Role role = roleList.get(i);
                if (role.getRid().equals(rid)){
                    roleList.remove(role);
                }
            }
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent()?user.get():null;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean update(User user) {
        Optional<User> dbInst = userRepository.findById(user.getUid());
        if (dbInst.isPresent()) {
            LOGGER.info("      db:" + dbInst.get());
            LOGGER.info("user    :" + user);
            BeanCopyUtil.beanCopy(user, dbInst.get());
            LOGGER.info("after db:" + dbInst.get());
            userRepository.save(dbInst.get());
            return true;
        }
        return false;
    }
}
