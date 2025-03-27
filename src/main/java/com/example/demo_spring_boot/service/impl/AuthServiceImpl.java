package com.example.demo_spring_boot.service.impl;

import com.example.demo_spring_boot.constant.CookieConfig;
import com.example.demo_spring_boot.dto.AuthRequest;
import com.example.demo_spring_boot.dto.AuthResponse;
import com.example.demo_spring_boot.dto.AuthResultDto;
import com.example.demo_spring_boot.exception.AuthenticationFailedException;
import com.example.demo_spring_boot.exception.InternalAuthException;
import com.example.demo_spring_boot.exception.InvalidTokenException;
import com.example.demo_spring_boot.exception.UnauthorizedTokenException;
import com.example.demo_spring_boot.service.AuthService;
import com.example.demo_spring_boot.service.ClientInfoService;
import com.example.demo_spring_boot.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ClientInfoService clientInfoService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private HashOperations<String, String, String> hashOps;

    @Autowired
    public void init() {
        this.hashOps = redisTemplate.opsForHash();
    }

    private String getRedisKey(String userId) {
        return "refresh_tokens:" + userId;
    }

    @Override
    public AuthResultDto authenticate(AuthRequest authRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            if (userDetails == null) {
                throw new AuthenticationFailedException("Không tìm thấy thông tin người dùng");
            }

            String userId = userDetails.getUsername(); // Hoặc ID thực tế của user
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            // Lấy thông tin client
            String clientIp = clientInfoService.getClientIp(request);
            String deviceInfo = clientInfoService.getDeviceInfo(request);

            // Lưu vào Redis dưới dạng Hash
            String value = clientIp + "|" + deviceInfo;
            hashOps.put(getRedisKey(userId), refreshToken, value);

            // Tạo cookie cho refreshToken
            Cookie cookie = CookieConfig.REFRESH_TOKEN_COOKIE.createCookie(refreshToken);

            // Trả về accessToken qua body
            return new AuthResultDto(accessToken, cookie);
        } catch (AuthenticationFailedException e) {
            throw e; // Ném lại lỗi nếu đã là lỗi xác thực
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

            String username;
            try {
                username = jwtService.extractUsername(refreshToken);
            } catch (Exception e) {
                throw new InvalidTokenException("Không thể trích xuất username từ refresh token");
            }

            UserDetails userDetails;
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                throw new InternalAuthException("Lỗi khi tải thông tin người dùng", e);
            }

            String userId = userDetails.getUsername();
            String redisKey = getRedisKey(userId);
            Map<String, String> userTokens = hashOps.entries(redisKey);

            if (!userTokens.containsKey(refreshToken)) {
                throw new UnauthorizedTokenException("Refresh token không tồn tại hoặc đã bị thu hồi");
            }

            String clientInfo = userTokens.get(refreshToken);
            String[] clientInfoArray = clientInfo.split("\\|");
            if (clientInfoArray.length != 2) {
                throw new UnauthorizedTokenException("Thông tin client không hợp lệ");
            }

            String clientIp = clientInfoArray[0];
            String deviceInfo = clientInfoArray[1];

            if (!clientIp.equals(clientInfoService.getClientIp(request)) ||
                    !deviceInfo.equals(clientInfoService.getDeviceInfo(request))) {
                throw new UnauthorizedTokenException("Thông tin client không khớp");
            }

            if (!jwtService.validateToken(refreshToken, userDetails)) {
                hashOps.delete(redisKey, refreshToken);
                throw new UnauthorizedTokenException("Refresh token không hợp lệ hoặc đã hết hạn");
            }

            String newAccessToken = jwtService.generateAccessToken(userDetails);
            return new AuthResponse(newAccessToken);

        } catch (InvalidTokenException | UnauthorizedTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthException("Lỗi không xác định khi xử lý refresh token", e);
        }
    }

}
