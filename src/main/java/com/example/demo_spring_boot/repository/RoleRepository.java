package com.example.demo_spring_boot.repository;

import java.security.Permission;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo_spring_boot.model.entity.Role;
import com.example.demo_spring_boot.model.entity.User;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);

  @Query("SELECT r FROM Role r JOIN r.users u WHERE u = :user")
  List<Role> findByUser(@Param("user") User user);

  @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p IN (:permissions)")
  List<Role> findByPermissions(@Param("permissions") Set<Permission> permissions);
}
