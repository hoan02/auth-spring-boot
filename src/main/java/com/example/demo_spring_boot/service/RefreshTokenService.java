package com.example.demo_spring_boot.service;

public interface RefreshTokenService {
    void saveRefreshToken(String userId, String token, String clientIp, String deviceInfo);

    void validateRefreshToken(String userId, String token, String clientIp, String deviceInfo);

    void revokeRefreshToken(String token);
}