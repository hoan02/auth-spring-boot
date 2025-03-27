package com.example.demo_spring_boot.dto;

import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResultDto {
  private String accessToken;
  private Cookie refreshTokenCookie;

  public AuthResultDto(String accessToken, Cookie refreshTokenCookie) {
        this.accessToken = accessToken;
        this.refreshTokenCookie = refreshTokenCookie;
    }
}
