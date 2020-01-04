package com.shadow.springboot.application.repository;

import com.shadow.springboot.application.domain.bo.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
}
