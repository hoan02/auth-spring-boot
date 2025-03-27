package com.example.demo_spring_boot.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo_spring_boot.constant.CookieConfig;
import com.example.demo_spring_boot.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

  @Autowired
  private JwtService jwtService;

  @SuppressWarnings("null")
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    String refreshToken = jwtService.getRefreshTokenFromRequest(request);
    if (refreshToken != null) {
      addRefreshTokenCookie(response, refreshToken);
    }
  }

  private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie cookie = CookieConfig.REFRESH_TOKEN_COOKIE.createCookie(refreshToken);
    response.addCookie(cookie);
  }
}
