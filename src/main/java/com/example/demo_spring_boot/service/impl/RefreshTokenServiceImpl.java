package com.example.demo_spring_boot.service.impl;

import com.example.demo_spring_boot.model.redis.RefreshToken;
import com.example.demo_spring_boot.repository.RefreshTokenRepository;
import com.example.demo_spring_boot.service.JwtService;
import com.example.demo_spring_boot.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private JwtService jwtService;

  @Override
  public void saveRefreshToken(String userId, String token, String clientIp, String deviceInfo) {
    RefreshToken refreshToken = new RefreshToken(userId, token, clientIp, deviceInfo);
    refreshTokenRepository.save(refreshToken);
  }

  @Override
  public void validateRefreshToken(String userId, String token, String clientIp, String deviceInfo) {
    Optional<RefreshToken> existingToken = refreshTokenRepository.findById(userId);
    if (existingToken.isEmpty() || !existingToken.get().getToken().equals(token)) {
      throw new RuntimeException("Refresh token không hợp lệ hoặc không tồn tại");
    }
  }

  @Override
  public void revokeRefreshToken(String token) {
    String username = jwtService.extractUsername(token);
    refreshTokenRepository.deleteById(username);
  }
}
