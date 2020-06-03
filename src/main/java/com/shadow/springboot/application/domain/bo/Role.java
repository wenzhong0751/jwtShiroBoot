package com.shadow.springboot.application.domain.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "sys_role")
public class Role {

    @Id
    private String rid;
    private String role;
    private String description;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_role_resource", joinColumns = {@JoinColumn(name = "rid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    private List<Resource> resourceList;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "rid")}, inverseJoinColumns = {@JoinColumn(name = "uid")})
    private List<User> userList;

    public Role() {
    }

    public Role(String rid, String role, String description) {
        this.rid = rid;
        this.role = role;
        this.description = description;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public String toString() {
        return "Role{" +
                "rid='" + rid + '\'' +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
