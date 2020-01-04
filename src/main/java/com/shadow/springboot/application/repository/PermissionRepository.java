package com.shadow.springboot.application.repository;

import com.shadow.springboot.application.domain.bo.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {
}
