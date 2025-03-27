package com.example.demo_spring_boot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
  private String token;
  private String tokenType = "Bearer";

  public AuthResponse(String token) {
    this.token = token;
  }
}