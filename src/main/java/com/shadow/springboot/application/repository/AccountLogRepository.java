package com.shadow.springboot.application.repository;

import com.shadow.springboot.application.domain.bo.AccountLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountLogRepository extends JpaRepository<AccountLog,Long> {
}
