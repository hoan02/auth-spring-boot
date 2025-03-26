package com.example.demo_spring_boot.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String generateToken(UserDetails userDetails);

  boolean validateToken(String token, UserDetails userDetails);

  String extractUsername(String token);
}
