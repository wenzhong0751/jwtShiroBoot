package com.shadow.springboot.application.service.impl;

import com.shadow.springboot.application.domain.bo.Resource;
import com.shadow.springboot.application.domain.bo.Role;
import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.domain.vo.RoleSearchVo;
import com.shadow.springboot.application.repository.ResourceRepository;
import com.shadow.springboot.application.repository.RoleRepository;
import com.shadow.springboot.application.repository.UserRepository;
import com.shadow.springboot.application.service.RoleService;
import com.shadow.springboot.application.util.BeanCopyUtil;
import com.shadow.springboot.application.util.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("roleServiceImpl")
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<Role> getPage(int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.ASC, "rid");
        if (pageNum > 0){
            pageNum -= 1;
        }
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return roleRepository.findAll(pageable);
    }

    @Override
    public Page<Role> getPage(int pageNum, int pageSize, RoleSearchVo searchVo) {
        Sort sort = new Sort(Sort.Direction.ASC, "rid");
        if (pageNum > 0){
            pageNum -= 1;
        }
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Specification<Role> specification = new Specification<Role>(){
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<Integer> status = root.get("status");
                Predicate p = criteriaBuilder.equal(status,searchVo.getStatus());
                return p;
            }
        };
        return roleRepository.findAll(specification,pageable);
    }

    @Override
    public Boolean addRole(Role role) {
        Role inst = roleRepository.save(role);
        if (inst != null) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateRole(Role role) {
        Optional<Role> dbInst = roleRepository.findById(role.getRid());
        if (dbInst.isPresent()) {
            BeanCopyUtil.beanCopy(role, dbInst.get());
            roleRepository.save(dbInst.get());
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteRoleByRoleId(String rid) {
        roleRepository.deleteById(rid);
        return true;
    }

    @Override
    public Boolean authorityRoleResource(String rid, Integer pid) {
        Optional<Role> dbInst = roleRepository.findById(rid);
        if (dbInst.isPresent()) {
            Optional<Resource> dbResource = resourceRepository.findById(pid);
            if (dbResource.isPresent()) {
                dbInst.get().getResourceList().add(dbResource.get());
                roleRepository.save(dbInst.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean deleteAuthorityRoleResource(String rid, Integer pid) {
        Optional<Role> dbInst = roleRepository.findById(rid);
        if (dbInst.isPresent()) {
            List<Resource> list = dbInst.get().getResourceList().stream().filter(item->!item.getRid().equals(pid)).collect(Collectors.toList());
            dbInst.get().setResourceList(list);
//            for (int i = 0; i < list.size(); i++) {
//                Permission p = list.get(i);
//                if (p.getPid() == pid) {
//                    list.remove(p);
//                    break;
//                }
//            }
            roleRepository.save(dbInst.get());
            return true;
        }
        return false;
    }

    @Override
    public Page<Resource> getAuthorityApisByRoleId(String rid, int pageNum, int pageSize) {
        Optional<Role> dbInst = roleRepository.findById(rid);
        if (dbInst.isPresent()) {
            List<Resource> list = dbInst.get().getResourceList().stream().filter(item->item.getResourceType().equals(2)).collect(Collectors.toList());
//            List<Resource> buttonList = new ArrayList<Permission>();
//            for (int i = 0; i < list.size(); i++) {
//                Permission p = list.get(i);
//                if (p.getResourceType().equals("button")) {
//                    buttonList.add(p);
//                }
//            }
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return PageHelper.listConvertToPage(list, pageable);
        }
        return null;
    }

    @Override
    public Page<Resource> getNotAuthorityApisByRoleId(String rid, int pageNum, int pageSize) {
        Optional<Role> dbInst = roleRepository.findById(rid);
        if (dbInst.isPresent()) {
            List<Resource> rList = dbInst.get().getResourceList();

            List<Resource> allList = resourceRepository.findAll();

            List<Resource> reduce = allList.stream().filter(item -> item.getResourceType().equals(2)).filter(item -> !rList.contains(item)).collect(Collectors.toList());
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return PageHelper.listConvertToPage(reduce, pageable);
        }
        return null;
    }

    @Override
    public Page<Resource> getAuthorityMenusByRoleId(String rid, int pageNum, int pageSize) {
        Optional<Role> dbInst = roleRepository.findById(rid);
        if (dbInst.isPresent()) {
            List<Resource> list = dbInst.get().getResourceList();
            List<Resource> reduce = list.stream().filter(item->item.getResourceType().equals(1)).collect(Collectors.toList());

            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return PageHelper.listConvertToPage(reduce, pageable);
        }
        return null;
    }

    @Override
    public Page<Resource> getNotAuthorityMenusByRoleId(String rid, int pageNum, int pageSize) {
        Optional<Role> dbInst = roleRepository.findById(rid);
        if (dbInst.isPresent()) {
            List<Resource> rList = dbInst.get().getResourceList();

            List<Resource> allList = resourceRepository.findAll();

            List<Resource> reduce = allList.stream().filter(item -> item.getResourceType().equals(1)).filter(item -> !rList.contains(item)).collect(Collectors.toList());
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return PageHelper.listConvertToPage(reduce, pageable);
        }
        return null;
    }

    @Override
    public Page<User> getUserListByRoleId(String rid, int pageNum, int pageSize) {
        Optional<Role> dbInst = roleRepository.findById(rid);
        if (dbInst.isPresent()) {
            List<User> list = dbInst.get().getUserList();

            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return PageHelper.listConvertToPage(list, pageable);
        }
        return null;
    }

    @Override
    public Page<User> getNotAuthorityUserListByRoleId(String rid, int pageNum, int pageSize) {
        Optional<Role> dbInst = roleRepository.findById(rid);
        if (dbInst.isPresent()) {
            List<User> list = dbInst.get().getUserList();

            List<User> allList = userRepository.findAll();

            List<User> reduce = allList.stream().filter(item->!list.contains(item)).collect(Collectors.toList());

            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return PageHelper.listConvertToPage(reduce, pageable);
        }
        return null;
    }

}
