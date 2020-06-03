package com.shadow.springboot.application.service.impl;

import com.shadow.springboot.application.domain.bo.Resource;
import com.shadow.springboot.application.domain.bo.Role;
import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.repository.ResourceRepository;
import com.shadow.springboot.application.repository.UserRepository;
import com.shadow.springboot.application.service.ResourceService;
import com.shadow.springboot.application.util.BeanCopyUtil;
import com.shadow.springboot.application.util.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("resourceServiceImpl")
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Resource> getMenus() {
        return resourceRepository.findAll().stream().filter(item -> item.getResourceType().equals(1)).collect(Collectors.toList());
    }

    @Override
    public List<Resource> getAuthorityMenusByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            List<Role> roleList = user.get().getRoleList();
            LOGGER.debug("roleList.size()=" + roleList.size());
            List<Resource> resourceList = new ArrayList<Resource>();
            for (Role role : roleList
            ) {
                resourceList.addAll(role.getResourceList());
            }
            LOGGER.debug("resourceList.size()=" + resourceList.size());
            return resourceList.stream().filter(item -> item.getResourceType().equals(1)).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Boolean addMenu(Resource resource) {
        Resource resource1 = resourceRepository.save(resource);
        if (resource1 != null) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean modifyMenu(Resource resource) {
        Optional<Resource> dbInst = resourceRepository.findById(resource.getRid());
        if (dbInst.isPresent()) {
            BeanCopyUtil.beanCopy(resource, dbInst.get());
            resourceRepository.save(dbInst.get());
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteMenuByMenuId(Integer rid) {
        resourceRepository.deleteById(rid);
        return true;
    }

    @Override
    public List<Resource> getApiTeamList() {
        return resourceRepository.findAll().stream().filter(item -> item.getResourceType().equals(3)).collect(Collectors.toList());
    }

    @Override
    public Page<Resource> getApiList(int pageNum, int pageSize) {
        List<Resource> list = resourceRepository.findAll().stream().filter(item -> item.getResourceType().equals(2)).collect(Collectors.toList());
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return PageHelper.listConvertToPage(list, pageable);
    }

    @Override
    public Page<Resource> getApiListByTeamId(Integer teamId, int pageNum, int pageSize) {
        if (pageNum > 0){
            pageNum -= 1;
        }
        List<Resource> list = resourceRepository.findAll().stream().filter(item -> item.getParentId().equals(teamId)).collect(Collectors.toList());
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return PageHelper.listConvertToPage(list, pageable);
    }
}
