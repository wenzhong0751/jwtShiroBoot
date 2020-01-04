package com.shadow.springboot.application.domain.bo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户账户操作日志
 *
 * @author tomsun28
 * @date 8:14 2018/4/22
 */
@Entity
@Table(name = "sys_account_log")
public class AccountLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String logName;
    private String userId;
    private String ip;
    private short succeed;
    private String message;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public short getSucceed() {
        return succeed;
    }

    public void setSucceed(short succeed) {
        this.succeed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
