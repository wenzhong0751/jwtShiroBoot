package com.shadow.springboot.application.service.impl;

import com.shadow.springboot.application.domain.bo.Role;
import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.repository.RoleRepository;
import com.shadow.springboot.application.repository.UserRepository;
import com.shadow.springboot.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

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
    public Set<String> getRoleSet(String username) {
        List<Role> roles = userRepository.findByUsername(username).getRoleList();
        Set<String> roleSet = new HashSet<String>();
        for (Role role : roles) {
            roleSet.add(role.getRid());
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
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}
