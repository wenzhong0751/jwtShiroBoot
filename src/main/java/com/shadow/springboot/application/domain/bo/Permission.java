package com.shadow.springboot.application.domain.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "sys_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = -1139725408205521288L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "permission_gen")
    @TableGenerator(name = "permission_gen",
            table = "sys_pk_generator",
            pkColumnName = "gen_name",
            valueColumnName = "gen_value",
            pkColumnValue = "sys_permission",
            allocationSize = 1)
    private Integer pid;
    private String name;
    private String resourceType;
    private String url;
    private String permission;
    private Integer parentId;
    private String parentIds;
    private Boolean disabled = Boolean.FALSE;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "sys_role_permission",joinColumns = {@JoinColumn(name = "pid")},inverseJoinColumns = {@JoinColumn(name = "rid")})
    private List<Role> roleList;

    public Permission() {
    }

    public Permission(String name, String resourceType, String url, String permission, Integer parentId, String parentIds) {
        this.name = name;
        this.resourceType = resourceType;
        this.url = url;
        this.permission = permission;
        this.parentId = parentId;
        this.parentIds = parentIds;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
