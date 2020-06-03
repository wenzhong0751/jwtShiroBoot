package com.shadow.springboot.application.domain.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "sys_resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 5134851771518893550L;

    public static final Integer TYPE_MENU = 1;
    public static final Integer TYPE_CATEGORY = 3;
    public static final Integer TYPE_API = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "resource_gen")
    @TableGenerator(name = "resource_gen",
            table = "sys_pk_generator",
            pkColumnName = "gen_name",
            valueColumnName = "gen_value",
            pkColumnValue = "sys_resource",
            allocationSize = 1)
    private Integer rid;
    private String code;
    private String name;
    private Integer parentId;
    private String uri;
    private Integer resourceType;
    private String method;
    private String icon;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_resource", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "rid")})
    private List<Role> roleList;

    public Resource() {
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "rid=" + rid +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", uri='" + uri + '\'' +
                ", resourceType=" + resourceType +
                ", method='" + method + '\'' +
                ", icon='" + icon + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", roleList=" + roleList +
                '}';
    }
}
