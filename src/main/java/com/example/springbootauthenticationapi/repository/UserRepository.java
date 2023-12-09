package com.example.springbootauthenticationapi.repository;

import com.example.springbootauthenticationapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
}
