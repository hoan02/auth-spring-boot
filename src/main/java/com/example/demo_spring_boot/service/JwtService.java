package com.example.demo_spring_boot.service;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
  String generateAccessToken(UserDetails userDetails);

  String generateRefreshToken(UserDetails userDetails);

  boolean validateToken(String token, UserDetails userDetails);

  String extractUsername(String token);

  boolean validateTokenStructure(String token);

  String getRefreshTokenFromRequest(HttpServletRequest request);
}
