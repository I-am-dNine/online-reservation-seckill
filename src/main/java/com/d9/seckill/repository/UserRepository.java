package com.d9.seckill.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d9.seckill.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
