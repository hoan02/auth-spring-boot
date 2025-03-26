package com.example.demo_spring_boot.repository;

import com.example.demo_spring_boot.model.entity.Permission;
import com.example.demo_spring_boot.model.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);

    List<Permission> findByUser(User user);
}