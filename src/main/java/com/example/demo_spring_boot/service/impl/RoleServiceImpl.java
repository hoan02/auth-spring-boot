package com.example.demo_spring_boot.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo_spring_boot.model.entity.Role;
import com.example.demo_spring_boot.repository.RoleRepository;
import com.example.demo_spring_boot.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    
  private final RoleRepository roleRepository;

  public RoleServiceImpl(RoleRepository roleRepository) {
      this.roleRepository = roleRepository;
  }

  @Override
  public Role createRole(String name, String description) {
      Role role = new Role();
      role.setName(name);
      role.setDescription(description);
      return roleRepository.save(role);
  }

  @Override
  public Optional<Role> getRoleByName(String name) {
      return roleRepository.findByName(name);
  }
}
