package com.shadow.springboot.application.service.impl;

import com.shadow.springboot.application.domain.bo.OperationLog;
import com.shadow.springboot.application.repository.OperationLogRepository;
import com.shadow.springboot.application.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service("operationLogServiceImpl")
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Override
    public Page<OperationLog> getOperationList(int pageNum, int pageSize) {
        if (pageNum > 0){
            pageNum -= 1;
        }
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return operationLogRepository.findAll(pageable);
    }
}
