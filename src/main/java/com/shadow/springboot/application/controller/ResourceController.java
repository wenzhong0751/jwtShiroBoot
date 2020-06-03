package com.shadow.springboot.application.controller;

import com.shadow.springboot.application.domain.vo.MenuTreeNode;
import com.shadow.springboot.application.domain.vo.Message;
import com.shadow.springboot.application.service.ResourceService;
import com.shadow.springboot.application.util.TreeUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseAction.class);

    @Resource(name = "resourceServiceImpl")
    private ResourceService resourceService;

    @ApiOperation(value = "获取用户被授权菜单",notes = "通过uid获取对应用户被授权的菜单列表,获取完整菜单树形结构")
    @GetMapping("authorityMenu")
    public Message getAuthorityMenu(HttpServletRequest request) {
        String username = request.getHeader("appId");
        List<MenuTreeNode> treeNodes = new ArrayList<>();
        List<com.shadow.springboot.application.domain.bo.Resource> resources = resourceService.getAuthorityMenusByUsername(username);

        for (com.shadow.springboot.application.domain.bo.Resource resource : resources) {
            MenuTreeNode node = new MenuTreeNode();
            BeanUtils.copyProperties(resource,node);
            treeNodes.add(node);
        }
        List<MenuTreeNode> menuTreeNodes = TreeUtil.buildTreeBy2Loop(treeNodes,-1);
        return new Message().ok(6666,"return menu list success").addData("menuTree",menuTreeNodes);
    }

    @ApiOperation(value = "获取全部菜单列", httpMethod = "GET")
    @GetMapping("menus")
    public Message getMenus() {

        List<MenuTreeNode> treeNodes = new ArrayList<>();
        List<com.shadow.springboot.application.domain.bo.Resource> resources = resourceService.getMenus();

        for (com.shadow.springboot.application.domain.bo.Resource resource: resources) {
            MenuTreeNode node = new MenuTreeNode();
            BeanUtils.copyProperties(resource,node);
            treeNodes.add(node);
        }
        List<MenuTreeNode> menuTreeNodes = TreeUtil.buildTreeBy2Loop(treeNodes,-1);
        return new Message().ok(6666,"return menus success").addData("menuTree",menuTreeNodes);
    }

    @ApiOperation(value = "增加菜单",httpMethod = "POST")
    @PostMapping("menu")
    public Message addMenu(@RequestBody com.shadow.springboot.application.domain.bo.Resource menu ) {

        Boolean flag = resourceService.addMenu(menu);
        if (flag) {
            return new Message().ok(6666,"add menu success");
        } else {
            return new Message().error(1111,"add menu fail");
        }
    }

    @ApiOperation(value = "修改菜单",httpMethod = "PUT")
    @PutMapping("menu")
    public Message updateMenu(@RequestBody com.shadow.springboot.application.domain.bo.Resource menu) {

        Boolean flag = resourceService.modifyMenu(menu);
        if (flag) {
            return new Message().ok(6666,"update menu success");
        } else {
            return new Message().error(1111, "update menu fail");
        }
    }

    @ApiOperation(value = "删除菜单", notes = "根据菜单ID删除菜单", httpMethod = "DELETE")
    @DeleteMapping("menu/{menuId}")
    public Message deleteMenuByMenuId(@PathVariable Integer menuId) {

        Boolean flag = resourceService.deleteMenuByMenuId(menuId);
        if (flag) {
            return new Message().ok(6666, "delete menu success");
        } else {
            return new Message().error(1111, "delete menu fail");
        }
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取API list", notes = "需要分页,根据teamId判断,-1->获取api分类,0->获取全部api,id->获取对应分类id下的api",httpMethod = "GET")
    @GetMapping("api/{teamId}/{currentPage}/{pageSize}")
    public Message getApiList(@PathVariable Integer teamId, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {

        List<com.shadow.springboot.application.domain.bo.Resource> resources = null;
        if (teamId == -1) {
            // -1 为获取api分类
            resources = resourceService.getApiTeamList();
            return new Message().ok(6666,"return apis success").addData("data",resources);
        }
        Page<com.shadow.springboot.application.domain.bo.Resource> pageInfo;
        if (teamId == 0) {
            // 0 为获取全部api
            pageInfo = resourceService.getApiList(currentPage,pageSize);
        } else {
            // 其他查询teamId 对应分类下的apis
            pageInfo = resourceService.getApiListByTeamId(teamId,currentPage,pageSize);
        }
        return new Message().ok(6666,"return apis success").addData("pageInfo",pageInfo);
    }

    @ApiOperation(value = "增加API",httpMethod = "POST")
    @PostMapping("api")
    public Message addApi(@RequestBody com.shadow.springboot.application.domain.bo.Resource api ) {

        Boolean flag = resourceService.addMenu(api);
        if (flag) {
            return new Message().ok(6666,"add api success");
        } else {
            return new Message().error(1111,"add api fail");
        }
    }

    @ApiOperation(value = "修改API",httpMethod = "PUT")
    @PutMapping("api")
    public Message updateApi(@RequestBody com.shadow.springboot.application.domain.bo.Resource api) {

        Boolean flag = resourceService.modifyMenu(api);
        if (flag) {
            return new Message().ok(6666,"update api success");
        } else {
            return new Message().error(1111, "update api fail");
        }
    }

    @ApiOperation(value = "删除API", notes = "根据API_ID删除API", httpMethod = "DELETE")
    @DeleteMapping("api/{apiId}")
    public Message deleteApiByApiId(@PathVariable Integer apiId) {

        Boolean flag = resourceService.deleteMenuByMenuId(apiId);
        if (flag) {
            return new Message().ok(6666, "delete api success");
        } else {
            return new Message().error(1111, "delete api fail");
        }
    }

}
