package com.example.demo_spring_boot.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo_spring_boot.service.ClientInfoService;
import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Client;
import ua_parser.Parser;

@Service
public class ClientInfoServiceImpl implements ClientInfoService {
  private final Parser uaParser = new Parser();

  // Lấy IP của client
  public String getClientIp(HttpServletRequest request) {
    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
    if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
      return xForwardedForHeader.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }

  // Lấy thông tin thiết bị từ User-Agent
  public String getDeviceInfo(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    if (userAgent != null && !userAgent.isEmpty()) {
      Client client = uaParser.parse(userAgent);
      if (client != null) {
        String browser = client.userAgent.family;
        String os = client.os.family;
        String device = client.device.family;
        return String.format("Browser: %s, OS: %s, Device: %s", browser, os, device);
      }
    }
    return "Unknown";
  }
}
