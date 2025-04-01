package com.example.demo_spring_boot.service;

import com.example.demo_spring_boot.dto.AuthRequest;
import com.example.demo_spring_boot.dto.AuthResponse;
import com.example.demo_spring_boot.dto.AuthResultDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    AuthResultDto authenticate(AuthRequest authRequest, HttpServletRequest request);

    AuthResponse refreshToken(String refreshToken, HttpServletRequest request);

    void logout(String token);
}