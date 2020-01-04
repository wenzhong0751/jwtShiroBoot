package com.shadow.springboot.application.service;

import com.shadow.springboot.application.domain.bo.OperationLog;
import org.springframework.data.domain.Page;

public interface OperationLogService {
    Page<OperationLog> getOperationList(int pageNum, int pageSize);
}
