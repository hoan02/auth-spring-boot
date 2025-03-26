package com.example.demo_spring_boot.service.impl;

import com.example.demo_spring_boot.dto.PermissionDto;
import com.example.demo_spring_boot.model.entity.Permission;
import com.example.demo_spring_boot.repository.PermissionRepository;
import com.example.demo_spring_boot.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<PermissionDto> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permission -> new PermissionDto(permission.getId(), permission.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public PermissionDto getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id).orElseThrow();
        return new PermissionDto(permission.getId(), permission.getName());
    }

    @Override
    public PermissionDto createPermission(PermissionDto permissionDto) {
        Permission permission = new Permission();
        permission.setName(permissionDto.getName());
        return new PermissionDto(permissionRepository.save(permission).getId(), permission.getName());
    }

    @Override
    public PermissionDto updatePermission(Long id, PermissionDto permissionDto) {
        Permission permission = permissionRepository.findById(id).orElseThrow();
        permission.setName(permissionDto.getName());
        return new PermissionDto(permissionRepository.save(permission).getId(), permission.getName());
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }
}