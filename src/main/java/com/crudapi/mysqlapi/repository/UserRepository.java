package com.crudapi.mysqlapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crudapi.mysqlapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
}
