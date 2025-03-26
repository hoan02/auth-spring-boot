package com.example.demo_spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo_spring_boot.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}
