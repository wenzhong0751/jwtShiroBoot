package com.shadow.springboot.application.shiro.provider.impl;

import com.google.common.base.Strings;
import com.shadow.springboot.application.domain.bo.Resource;
import com.shadow.springboot.application.repository.ResourceRepository;
import com.shadow.springboot.application.repository.RoleRepository;
import com.shadow.springboot.application.shiro.provider.ShiroFilterRulesProvider;
import com.shadow.springboot.application.shiro.rule.RolePermRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author tomsun28
 * @date 16:46 2018/3/7
 */
@Service("shiroFilterRulesProviderImpl")
public class ShiroFilterRulesProviderImpl implements ShiroFilterRulesProvider {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RolePermRule> loadRolePermRules() {
        List<Resource> resourceList = resourceRepository.findAll().stream().filter(item -> item.getResourceType().equals(2)).collect(Collectors.toList());
        List<RolePermRule> list = new ArrayList<RolePermRule>();
        for (Resource resource : resourceList
        ) {
            String url = Strings.nullToEmpty(resource.getUri()) + "==" + Strings.nullToEmpty(resource.getMethod());
            String roles = resource.getRoleList().stream().map(p -> p.getRid()).collect(Collectors.joining(","));
            RolePermRule rolePermRule = new RolePermRule(url, roles);
            list.add(rolePermRule);
        }
        return list;
    }


    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
