package com.shadow.springboot.application.repository;

import com.shadow.springboot.application.domain.bo.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationLogRepository extends JpaRepository<OperationLog,Long> {
}
