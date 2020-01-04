package com.shadow.springboot.application;

import com.shadow.springboot.application.domain.bo.AccountLog;
import com.shadow.springboot.application.domain.bo.OperationLog;
import com.shadow.springboot.application.repository.AccountLogRepository;
import com.shadow.springboot.application.repository.OperationLogRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountLogTests {

    @Autowired
    AccountLogRepository accountLogRepository;

    @Autowired
    OperationLogRepository operationLogRepository;

    @Test
    public void testAdd(){
        //
        AccountLog log = new AccountLog();
        log.setIp("192.168.0.164");
        log.setLogName("testName");
        log.setMessage("testMessage");
        log.setSucceed((short)1);
//        log.setUserId(1L);
        log.setCreateTime(new Date());
        accountLogRepository.save(log);
    }

    @Test
    public void testAddOperationLog(){
        OperationLog log = new OperationLog();
        log.setApi("testApi");
        log.setCreateTime(new Date());
        log.setLogName("testLogName");
        log.setMessage("testMessage");
        log.setMethod("post");
        log.setSucceed((short)1);
//        log.setUserId(1L);
        operationLogRepository.save(log);
    }
}
