package com.example.demo_spring_boot.repository;

import com.example.demo_spring_boot.model.entity.Permission;
import com.example.demo_spring_boot.model.entity.Role;
import com.example.demo_spring_boot.model.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);

    @Query("SELECT p FROM Permission p JOIN p.roles r JOIN r.users u WHERE u = :user")
    List<Permission> findByUser(@Param("user") User user);

    @Query("SELECT p FROM Permission p JOIN p.roles r WHERE r IN (:roles)")
    List<Permission> findByRoles(@Param("roles") Set<Role> roles);
}