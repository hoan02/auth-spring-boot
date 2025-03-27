package com.example.demo_spring_boot.controller;

import com.example.demo_spring_boot.constant.CookieConfig;
import com.example.demo_spring_boot.dto.AuthRequest;
import com.example.demo_spring_boot.dto.AuthResponse;
import com.example.demo_spring_boot.dto.AuthResultDto;
import com.example.demo_spring_boot.dto.UserDto;
import com.example.demo_spring_boot.service.AuthService;
import com.example.demo_spring_boot.service.JwtService;
import com.example.demo_spring_boot.service.UserService;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        AuthResultDto authResult = authService.authenticate(authRequest);

        // Gắn refresh token vào cookie trong response
        response.addCookie(authResult.getRefreshTokenCookie());

        // Chỉ trả về access token
        return ResponseEntity.ok(new AuthResponse(authResult.getAccessToken()));
    }

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue("refresh-token") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}