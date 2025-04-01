package com.example.demo_spring_boot.service.impl;

import com.example.demo_spring_boot.model.entity.User;
import com.example.demo_spring_boot.model.entity.Role;
import com.example.demo_spring_boot.model.entity.Group;
import com.example.demo_spring_boot.model.entity.Permission;
import com.example.demo_spring_boot.repository.UserRepository;
import com.example.demo_spring_boot.repository.RoleRepository;
import com.example.demo_spring_boot.repository.GroupRepository;
import com.example.demo_spring_boot.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Không tìm thấy: " + username));
        List<Role> roles = roleRepository.findByUser(user);
        List<Group> groups = groupRepository.findByUser(user);
        List<Permission> permissions = permissionRepository.findByUser(user);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        for (Group group : groups) {
            authorities.add(new SimpleGrantedAuthority(group.getName()));
        }
        for (Permission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }
}