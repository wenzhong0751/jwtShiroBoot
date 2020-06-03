package com.shadow.springboot.application.controller;

import com.shadow.springboot.application.domain.bo.Role;
import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.domain.vo.Message;
import com.shadow.springboot.application.domain.vo.UserSearchVo;
import com.shadow.springboot.application.service.RoleService;
import com.shadow.springboot.application.service.UserService;
import com.shadow.springboot.application.support.factory.LogTaskFactory;
import com.shadow.springboot.application.support.manager.LogExeManager;
import com.shadow.springboot.application.util.CommonUtil;
import com.shadow.springboot.application.util.Md5Util;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户相关操作
 *
 * @author tomsun28
 * @date 21:05 2018/3/17
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    @ApiOperation(value = "获取对应用户角色", notes = "GET根据用户的appId获取对应用户的角色")
    @GetMapping("/role/{appId}")
    public Message getUserRoleList(@PathVariable String appId) {
        LOGGER.debug("getUserRoleList called");
        Set<String> roleSet = userService.getRoleSet(appId);

        LOGGER.info(roleSet.toString());
        return new Message().ok(6666, "return roles success").addData("roles", roleSet);
    }

    @ApiOperation(value = "获取用户列表", notes = "GET获取所有注册用户的信息列表")
    @GetMapping("/list/{start}/{limit}")
    public Message getUserList(@PathVariable Integer start, @PathVariable Integer limit,HttpServletRequest request) {
        LOGGER.info("userList called.");
        Map<String, String> map = getRequestParameter(request);
        Page<User> userPage;
        if (map.size() > 0){
            UserSearchVo searchVo = new UserSearchVo();
            String username = map.get("username");
            if (!StringUtils.isEmpty(username)){
                searchVo.setUsername(username);
            }
            userPage = userService.getPage(start,limit,searchVo);
        }else {
            userPage = userService.getPage(start, limit);
        }
        return new Message().ok(6666, "return user list success").addData("pageInfo", userPage);
    }

    @ApiOperation(value = "给用户授权添加角色", httpMethod = "POST")
    @PostMapping("/authority/role")
    public Message authorityUserRole(HttpServletRequest request) {
        Map<String, String> map = getRequestBody(request);
        String rid = map.get("rid");
        Long uid = Long.parseLong(map.get("uid"));
        boolean flag = userService.authority(uid, rid);

        return flag ? new Message().ok(6666, "authority success") : new Message().error(1111, "authority error");
    }

    @ApiOperation(value = "给用户授权添加角色列表", httpMethod = "PUT")
    @PutMapping("/authority/rolelist")
    public Message authorityUserRoleList(HttpServletRequest request) {
        Map<String, String> map = getRequestBody(request);
        String rids = map.get("rids");
        Long lUid = Long.parseLong(map.get("uid"));
        boolean flag = userService.authorityList(lUid, rids);

        return flag ? new Message().ok(6666, "authority success") : new Message().error(1111, "authority roleList error");
    }

    @ApiOperation(value = "删除已经授权的用户角色", httpMethod = "DELETE")
    @DeleteMapping("/authority/role/{uid}/{rid}")
    public Message deleteAuthorityUserRole(@PathVariable Long uid, @PathVariable String rid) {
        return userService.deleteAuthority(uid, rid) ? new Message().ok(6666, "delete success") : new Message().error(1111, "delete fail");
    }


    @ApiOperation(value = "用户登出", httpMethod = "POST")
    @PostMapping("/exit")
    public Message accountExit(HttpServletRequest request) {
        SecurityUtils.getSubject().logout();
        Map<String, String> map = getRequestHeader(request);
        String appId = map.get("appid");
        if (StringUtils.isEmpty(appId)) {
            return new Message().error(1111, "用户未登录无法登出1");
        }
        String jwt = redisTemplate.opsForValue().get("JWT-SESSION-" + appId);
        if (StringUtils.isEmpty(jwt)) {
            return new Message().error(1111, "用户未登录无法登出2");
        }
        redisTemplate.opsForValue().getOperations().delete("JWT-SESSION-" + appId);
        LogExeManager.getInstance().executeLogTask(LogTaskFactory.exitLog(appId, request.getRemoteAddr(), (short) 1, ""));

        return new Message().ok(6666, "用户退出成功");
    }

    @ApiOperation(value = "更新用户", httpMethod = "PUT")
    @PutMapping()
    public Message updateUser(@RequestBody User user) {

        boolean flag = userService.update(user);
        if (flag) {
            return new Message().ok(6666, "update success");
        } else {
            return new Message().error(1111, "update fail");
        }
    }

    @ApiOperation(value = "新增用户", httpMethod = "POST")
    @PostMapping()
    public  Message addUser(@RequestBody User user){
        String salt = CommonUtil.getRandomString(6);
        user.setSalt(salt);
        String newPwd = Md5Util.md5( user.getPassword() + salt);
        user.setPassword(newPwd);
        user.setRegtime(new Date());
        User rtnUser = userService.save(user);

        if (rtnUser.getUid() > 0) {
            return new Message().ok(6666, "update success");
        } else {
            return new Message().error(1111, "update fail");
        }
    }
}