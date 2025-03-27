package com.example.demo_spring_boot.service;

import com.example.demo_spring_boot.dto.AuthRequest;
import com.example.demo_spring_boot.dto.AuthResponse;
import com.example.demo_spring_boot.dto.AuthResultDto;

public interface AuthService {

    AuthResultDto authenticate(AuthRequest authRequest);

    AuthResponse refreshToken(String refreshToken);
}