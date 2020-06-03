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
        if (pageNum > 0){
            pageNum -= 1;
        }
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> getPage(int pageNum, int pageSize, UserSearchVo searchVo) {
        Sort sort = new Sort(Sort.Direction.ASC, "uid");
        if (pageNum > 0){
            pageNum -= 1;
        }
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
    public Boolean authorityList(Long uid, String rids) {
        Optional<User> user = userRepository.findById(uid);
        if (rids.length() < 1){
            rids = "role_anon";
        }
        String[] ridArr = rids.split(",");
        if (user.isPresent()) {
            Specification<Role> specification = new Specification<Role>(){
                @Override
                public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    Path<String> ridPath = root.get("rid");
                    List<Predicate> listOr = new ArrayList<Predicate>();
                    for (String rid:ridArr
                         ) {
                        listOr.add(criteriaBuilder.equal(ridPath,rid));
                    }
                    Predicate[] arrayOr = new Predicate[listOr.size()];
                    Predicate pre_or = criteriaBuilder.or(listOr.toArray(arrayOr));
                    return pre_or;
                }
            };
            List<Role> roleList = roleRepository.findAll(specification);
            user.get().setRoleList(roleList);
            userRepository.save(user.get());
            return true;
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
        // 新增用户，如果没有分配角色，则指定默认的“非角色”
        if (user.getRoleList() == null){
            Optional<Role> role = roleRepository.findById("role_anon");
            if (role.isPresent()){
                List<Role> list = new ArrayList<Role>();
                list.add(role.get());
                user.setRoleList(list);
            }
        }
        return userRepository.save(user);
    }

    @Override
    public Boolean update(User user) {
        Optional<User> dbInst = userRepository.findById(user.getUid());
        if (dbInst.isPresent()) {
            BeanCopyUtil.beanCopy(user, dbInst.get());
            userRepository.save(dbInst.get());
            return true;
        }
        return false;
    }
}
