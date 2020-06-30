package com.shadow.springboot.application.service.impl;

import com.shadow.springboot.application.domain.bo.AccountLog;
import com.shadow.springboot.application.repository.AccountLogRepository;
import com.shadow.springboot.application.service.AccountLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author tomsun28
 * @date 9:32 2018/4/22
 */
@Service("accountLogServiceImpl")
public class AccountLogServiceImpl implements AccountLogService {

    @Autowired
    AccountLogRepository accountLogRepository;

    @Override
    public Page<AccountLog> getAccountLogList(int pageNum, int pageSize) {
        if (pageNum > 0){
            pageNum -= 1;
        }
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(pageNum, pageSize,sort);
        return accountLogRepository.findAll(pageable);
    }
}
