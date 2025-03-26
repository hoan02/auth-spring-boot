package com.example.demo_spring_boot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo_spring_boot.model.entity.Role;
import com.example.demo_spring_boot.model.entity.User;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);

  List<Role> findByUser(User user);
}
