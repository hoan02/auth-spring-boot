package com.example.demo_spring_boot.service;

import com.example.demo_spring_boot.dto.AuthRequest;
import com.example.demo_spring_boot.dto.AuthResponse;

public interface AuthService {
  AuthResponse authenticate(AuthRequest request);
}
