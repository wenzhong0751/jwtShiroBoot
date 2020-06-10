package com.shadow.springboot.application.shiro.filter;

import com.shadow.springboot.application.service.AccountService;
import com.shadow.springboot.application.shiro.config.RestPathMatchingFilterChainResolver;
import com.shadow.springboot.application.shiro.provider.ShiroFilterRulesProvider;
import com.shadow.springboot.application.shiro.rule.RolePermRule;
import com.shadow.springboot.application.support.SpringContextHolder;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Filter 管理器
 *
 * @author tomsun28
 * @date 11:16 2018/2/28
 */
@Component
public class ShiroFilterChainManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroFilterChainManager.class);

    private final ShiroFilterRulesProvider shiroFilterRulesProvider;
    private final StringRedisTemplate redisTemplate;
    private final AccountService accountService;

    @Autowired
    public ShiroFilterChainManager(@Qualifier("shiroFilterRulesProviderImpl") ShiroFilterRulesProvider shiroFilterRulesProvider, StringRedisTemplate redisTemplate, @Qualifier("accountServiceImpl") AccountService accountService) {
        this.shiroFilterRulesProvider = shiroFilterRulesProvider;
        this.redisTemplate = redisTemplate;
        this.accountService = accountService;
    }

    /**
     * description 初始化获取过滤链
     *
     * @return java.util.Map<java.lang.String               ,               javax.servlet.Filter>
     */
    public Map<String, Filter> initGetFilters() {
        LOGGER.debug("initGetFilters be called");
        Map<String, Filter> filters = new LinkedHashMap<>();
        PasswordFilter passwordFilter = new PasswordFilter();
        passwordFilter.setRedisTemplate(redisTemplate);
//        passwordFilter.setAccountService(accountService);
        filters.put("auth", passwordFilter);
        BonJwtFilter jwtFilter = new BonJwtFilter();
        jwtFilter.setRedisTemplate(redisTemplate);
        jwtFilter.setAccountService(accountService);
        filters.put("jwt", jwtFilter);
        return filters;
    }

    /**
     * description 初始化获取过滤链规则
     *
     * @return java.util.Map<java.lang.String               ,               java.lang.String>
     */
    public Map<String, String> initGetFilterChain() {
        LOGGER.debug("initGetFilterChain be called");
        Map<String, String> filterChain = new LinkedHashMap<>();
        // -------------anon 默认过滤器忽略的URL
        List<String> defalutAnon = Arrays.asList("/css/**", "/js/**");
        defalutAnon.forEach(ignored -> filterChain.put(ignored, "anon"));
        // -------------auth 默认需要认证过滤器的URL 走auth--PasswordFilter
//        List<String> defalutAuth = Arrays.asList("/account/**","/account/login");
        List<String> defalutAuth = Arrays.asList("/account/login","/account/register","/account/jwt");
        defalutAuth.forEach(auth -> filterChain.put(auth, "auth"));
        // -------------dynamic 动态URL
        if (shiroFilterRulesProvider != null) {
            List<RolePermRule> rolePermRules = this.shiroFilterRulesProvider.loadRolePermRules();
//            LOGGER.debug("rolePermRules.size=" + rolePermRules.size());

            if (null != rolePermRules) {
                rolePermRules.forEach(rule -> {
                    LOGGER.debug("rolePermRule:" + rule.toString());
                    StringBuilder chain = rule.toFilterChain();
//                    LOGGER.debug("chain=" + chain.toString() + ";url=" + rule.getUrl());
                    if (null != chain) {
                        filterChain.putIfAbsent(rule.getUrl(), chain.toString());
                    }
                });
            }
        }
        filterChain.put("/**","jwt");
        return filterChain;
    }

    /**
     * description 动态重新加载过滤链规则
     */
    public void reloadFilterChain() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = SpringContextHolder.getBean(ShiroFilterFactoryBean.class);
        AbstractShiroFilter abstractShiroFilter = null;
        try {
            abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            RestPathMatchingFilterChainResolver filterChainResolver = (RestPathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
            DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
            filterChainManager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(this.initGetFilterChain());
            shiroFilterFactoryBean.getFilterChainDefinitionMap().forEach((k, v) -> filterChainManager.createChain(k, v));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
