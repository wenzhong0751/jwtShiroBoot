package com.shadow.springboot.application.domain.vo;

public class RoleSearchVo {
    Integer status;
    String rid;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "RoleSearchVo{" +
                "status=" + status +
                ", rid='" + rid + '\'' +
                '}';
    }
}
