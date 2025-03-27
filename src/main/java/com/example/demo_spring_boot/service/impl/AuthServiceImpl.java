package com.example.demo_spring_boot.service.impl;

import com.example.demo_spring_boot.constant.CookieConfig;
import com.example.demo_spring_boot.dto.AuthRequest;
import com.example.demo_spring_boot.dto.AuthResponse;
import com.example.demo_spring_boot.dto.AuthResultDto;
import com.example.demo_spring_boot.service.AuthService;
import com.example.demo_spring_boot.service.JwtService;

import jakarta.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public AuthResultDto authenticate(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Tạo cookie cho refreshToken
        Cookie cookie = CookieConfig.REFRESH_TOKEN_COOKIE.createCookie(refreshToken);

        // Trả về accessToken qua body
        return new AuthResultDto(accessToken, cookie);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (jwtService.validateTokenStructure(refreshToken)) {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(refreshToken, userDetails)) {
                String newAccessToken = jwtService.generateAccessToken(userDetails);

                return new AuthResponse(newAccessToken);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token không hợp lệ");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token không hợp lệ");
        }
    }
}