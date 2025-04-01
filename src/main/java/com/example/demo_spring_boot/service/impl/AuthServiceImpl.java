package com.example.demo_spring_boot.service.impl;

import com.example.demo_spring_boot.constant.CookieConfig;
import com.example.demo_spring_boot.dto.AuthRequest;
import com.example.demo_spring_boot.dto.AuthResponse;
import com.example.demo_spring_boot.dto.AuthResultDto;
import com.example.demo_spring_boot.exception.AuthenticationFailedException;
import com.example.demo_spring_boot.exception.InvalidTokenException;
import com.example.demo_spring_boot.exception.UnauthorizedTokenException;
import com.example.demo_spring_boot.service.AuthService;
import com.example.demo_spring_boot.service.ClientInfoService;
import com.example.demo_spring_boot.service.JwtService;
import com.example.demo_spring_boot.service.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ClientInfoService clientInfoService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public AuthResultDto authenticate(AuthRequest authRequest, HttpServletRequest request) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String userId = userDetails.getUsername();
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            String clientIp = clientInfoService.getClientIp(request);
            String deviceInfo = clientInfoService.getDeviceInfo(request);

            refreshTokenService.saveRefreshToken(userId, refreshToken, clientIp, deviceInfo);

            Cookie cookie = CookieConfig.REFRESH_TOKEN_COOKIE.createCookie(refreshToken);

            return new AuthResultDto(accessToken, cookie);
        } catch (AuthenticationFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationFailedException("Lỗi xác thực: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse refreshToken(String refreshToken, HttpServletRequest request) {
        try {
            if (!jwtService.validateTokenStructure(refreshToken)) {
                throw new InvalidTokenException("Refresh token có cấu trúc không hợp lệ");
            }

            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String userId = userDetails.getUsername();

            String clientIp = clientInfoService.getClientIp(request);
            String deviceInfo = clientInfoService.getDeviceInfo(request);

            refreshTokenService.validateRefreshToken(userId, refreshToken, clientIp, deviceInfo);

            if (!jwtService.validateToken(refreshToken, userDetails)) {
                refreshTokenService.revokeRefreshToken(refreshToken);
                throw new UnauthorizedTokenException("Refresh token không hợp lệ hoặc đã hết hạn");
            }

            String newAccessToken = jwtService.generateAccessToken(userDetails);
            return new AuthResponse(newAccessToken);

        } catch (InvalidTokenException | UnauthorizedTokenException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Lỗi xử lý refresh token", e);
            throw new AuthenticationFailedException("Lỗi khi xử lý refresh token: " + e.getMessage());
        }
    }

    @Override
    public void logout(String token) {
        refreshTokenService.revokeRefreshToken(token);
    }
}
