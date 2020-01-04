package com.shadow.springboot.application.repository;

import com.shadow.springboot.application.domain.bo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByUsernameOrEmail(String username, String email);
}
