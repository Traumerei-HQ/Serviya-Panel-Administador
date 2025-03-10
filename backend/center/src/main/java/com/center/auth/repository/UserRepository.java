package com.center.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.center.auth.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

