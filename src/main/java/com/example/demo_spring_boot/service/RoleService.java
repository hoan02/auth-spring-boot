package com.example.demo_spring_boot.service;

import java.util.Optional;

import com.example.demo_spring_boot.model.entity.Role;

public interface RoleService {
  Role createRole(String name, String description);
  Optional<Role> getRoleByName(String name);
}
