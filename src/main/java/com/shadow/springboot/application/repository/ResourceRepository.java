package com.shadow.springboot.application.repository;

import com.shadow.springboot.application.domain.bo.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource,Integer> {
}
