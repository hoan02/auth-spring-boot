package com.example.demo_spring_boot.service.impl;

import com.example.demo_spring_boot.dto.RoleDto;
import com.example.demo_spring_boot.model.entity.Role;
import com.example.demo_spring_boot.repository.RoleRepository;
import com.example.demo_spring_boot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new RoleDto(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow();
        return new RoleDto(role.getId(), role.getName());
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());
        return new RoleDto(roleRepository.save(role).getId(), role.getName());
    }

    @Override
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role role = roleRepository.findById(id).orElseThrow();
        role.setName(roleDto.getName());
        return new RoleDto(roleRepository.save(role).getId(), role.getName());
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}