package com.example.demo_spring_boot.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.demo_spring_boot.model.entity.User;

public interface UserService extends UserDetailsService {
  User registerUser(String username, String password, String email);
}