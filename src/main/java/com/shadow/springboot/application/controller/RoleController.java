package com.shadow.springboot.application.controller;

import com.shadow.springboot.application.domain.bo.Role;
import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.domain.vo.Message;
import com.shadow.springboot.application.domain.vo.RoleSearchVo;
import com.shadow.springboot.application.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * @author tomsun28
 * @date 20:02 2018/3/20
 */
@RequestMapping("/role")
@RestController
public class RoleController extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取角色LIST", httpMethod = "GET")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Message getRoles(@PathVariable Integer pageNum, @PathVariable Integer pageSize,HttpServletRequest request) {
        Page<Role> rolePage;

        Map<String, String> map = getRequestParameter(request);
        if (map.size() > 0){
            RoleSearchVo searchVo = new RoleSearchVo();
            String tmp = map.get("status");
            if (!StringUtils.isEmpty(tmp)){
                int status = Integer.parseInt(tmp);
                searchVo.setStatus(status);
            }
            if (!StringUtils.isEmpty(map.get("rid"))){
                searchVo.setRid(map.get("rid"));
            }
            rolePage = roleService.getPage(pageNum, pageSize, searchVo);
        }else{
            rolePage = roleService.getPage(pageNum, pageSize);
        }

        return new Message().ok(6666, "return user list success").addData("pageInfo", rolePage);
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取角色关联的(roleId)对应用户列表", httpMethod = "GET")
    @GetMapping("user/{roleId}/{currentPage}/{pageSize}")
    public Message getUserListByRoleId(@PathVariable String roleId, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        Page<User> pageInfo = roleService.getUserListByRoleId(roleId, currentPage, pageSize);
        return new Message().ok(6666, "return users success").addData("data", pageInfo);
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取角色未关联的用户列表", httpMethod = "GET")
    @GetMapping("user/-/{roleId}/{currentPage}/{pageSize}")
    public Message getUserListExtendByRoleId(@PathVariable String roleId, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        Page<User> pageInfo = roleService.getNotAuthorityUserListByRoleId(roleId, currentPage, pageSize);
        return new Message().ok(6666, "return users success").addData("data", pageInfo);
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取角色(roleId)所被授权的API资源")
    @GetMapping("api/{roleId}/{currentPage}/{pageSize}")
    public Message getRestApiExtendByRoleId(@PathVariable String roleId, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        LOGGER.info("roleId:" + roleId);
        Page<com.shadow.springboot.application.domain.bo.Resource> permissionPage = roleService.getAuthorityApisByRoleId(roleId, currentPage, pageSize);
        return new Message().ok(6666, "return api success").addData("pageInfo", permissionPage);
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取角色(roleId)未被授权的API资源")
    @GetMapping("api/-/{roleId}/{currentPage}/{pageSize}")
    public Message getRestApiByRoleId(@PathVariable String roleId, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        Page<com.shadow.springboot.application.domain.bo.Resource> permissionPage = roleService.getNotAuthorityApisByRoleId(roleId, currentPage, pageSize);
        return new Message().ok(6666, "return api success").addData("pageInfo", permissionPage);
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取角色(roleId)所被授权的menu资源")
    @GetMapping("menu/{roleId}/{currentPage}/{pageSize}")
    public Message getMenusByRoleId(@PathVariable String roleId, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        Page<com.shadow.springboot.application.domain.bo.Resource> pageInfo = roleService.getAuthorityMenusByRoleId(roleId, currentPage, pageSize);
        return new Message().ok(6666, "return api success").addData("pageInfo", pageInfo);
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取角色(roleId)未被授权的menu资源")
    @GetMapping("menu/-/{roleId}/{currentPage}/{pageSize}")
    public Message getMenusExtendByRoleId(@PathVariable String roleId, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        Page<com.shadow.springboot.application.domain.bo.Resource> pageInfo = roleService.getNotAuthorityMenusByRoleId(roleId, currentPage, pageSize);
        return new Message().ok(6666, "return api success").addData("data", pageInfo);
    }

    @ApiOperation(value = "授权资源给角色", httpMethod = "POST")
    @PostMapping("/authority/resource")
    public Message authorityRoleResource(HttpServletRequest request) {
        Map<String, String> map = getRequestBody(request);
        String roleId = map.get("roleId");
        int resourceId = Integer.parseInt(map.get("resourceId"));
        boolean flag = roleService.authorityRoleResource(roleId, resourceId);
        return flag ? new Message().ok(6666, "authority success") : new Message().error(1111, "authority error");
    }

    @ApiOperation(value = "给角色授权资源列表", httpMethod = "POST")
    @PutMapping("/authority/resourcelist")
    public Message authorityRoleResourceList(HttpServletRequest request) {
        Map<String, String> map = getRequestBody(request);
        String roleId = map.get("roleId");
        String rids = map.get("rids");
        String type = map.get("type");

        boolean flag = roleService.authorityList(roleId,rids,Integer.parseInt(type));
        return flag ? new Message().ok(6666, "authority success") : new Message().error(1111, "authority error");
    }

    @ApiOperation(value = "删除对应的角色的授权资源", httpMethod = "DELETE")
    @DeleteMapping("/authority/resource/{roleId}/{resourceId}")
    public Message deleteAuthorityRoleResource(@PathVariable String roleId, @PathVariable Integer resourceId) {
        boolean flag = roleService.deleteAuthorityRoleResource(roleId, resourceId);
//        shiroFilterChainManager.reloadFilterChain();
        return flag ? new Message().ok(6666, "authority success") : new Message().error(1111, "authority error");
    }

    @ApiOperation(value = "添加角色", httpMethod = "POST")
    @PostMapping()
    public Message addRole(@RequestBody Role role) {
        Optional<Role> dbRole = roleService.getRoleById(role.getRid());
        if (dbRole.isPresent()){
            return new Message().error(111, "角色代码已经存在，不能重复使用！");
        }

        boolean flag = roleService.addRole(role);
        if (flag) {
            return new Message().ok(6666, "add role success");
        } else {
            return new Message().error(111, "add role fail");
        }
    }

    @ApiOperation(value = "更新角色", httpMethod = "PUT")
    @PutMapping()
    public Message updateRole(@RequestBody Role role) {

        boolean flag = roleService.updateRole(role);
        if (flag) {
            return new Message().ok(6666, "update success");
        } else {
            return new Message().error(1111, "update fail");
        }
    }

    @ApiOperation(value = "根据角色ID删除角色", httpMethod = "DELETE")
    @DeleteMapping("{roleId}")
    public Message deleteRoleByRoleId(@PathVariable String roleId) {

        boolean flag = roleService.deleteRoleByRoleId(roleId);
        if (flag) {
            return new Message().ok(6666, "delete success");
        } else {
            return new Message().error(1111, "delete fail");
        }
    }

}
