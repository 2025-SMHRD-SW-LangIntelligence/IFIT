package com.ParQ.ParQ.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ParQ.ParQ.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
}
