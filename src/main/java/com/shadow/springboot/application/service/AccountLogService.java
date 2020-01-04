package com.shadow.springboot.application.service;

import com.shadow.springboot.application.domain.bo.AccountLog;
import org.springframework.data.domain.Page;

public interface AccountLogService {
    Page<AccountLog> getAccountLogList(int pageNum, int pageSize);
}
