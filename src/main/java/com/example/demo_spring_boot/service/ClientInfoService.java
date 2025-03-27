package com.example.demo_spring_boot.service;

import jakarta.servlet.http.HttpServletRequest;

public interface ClientInfoService {
  String getClientIp(HttpServletRequest request);

  String getDeviceInfo(HttpServletRequest request);
}
