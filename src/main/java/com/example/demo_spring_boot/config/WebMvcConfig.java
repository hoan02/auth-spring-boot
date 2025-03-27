package com.example.demo_spring_boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo_spring_boot.interceptor.RefreshTokenInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  private RefreshTokenInterceptor refreshTokenInterceptor;

  @SuppressWarnings("null")
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(refreshTokenInterceptor)
        .addPathPatterns("/api/**") // chỉ gắn cookie cho đường này
        .excludePathPatterns("/api/auth/register"); // loại trừ đường dẫn
  }
}
