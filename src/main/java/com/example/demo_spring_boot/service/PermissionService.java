package com.example.demo_spring_boot.service;

import com.example.demo_spring_boot.dto.PermissionDto;

import java.util.List;

public interface PermissionService {

    List<PermissionDto> getAllPermissions();

    PermissionDto getPermissionById(Long id);

    PermissionDto createPermission(PermissionDto permissionDto);

    PermissionDto updatePermission(Long id, PermissionDto permissionDto);

    void deletePermission(Long id);
}