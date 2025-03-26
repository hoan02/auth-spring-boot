package com.example.demo_spring_boot.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo_spring_boot.model.entity.Role;
import com.example.demo_spring_boot.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u JOIN u.roles r WHERE r IN (:roles)")
  List<User> findByRoles(@Param("roles") Set<Role> roles);
}
