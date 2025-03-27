package com.example.demo_spring_boot.constant;

import jakarta.servlet.http.Cookie;

public enum CookieConfig {
  REFRESH_TOKEN_COOKIE("refresh-token", "/", "localhost", true, true, 7 * 24 * 60 * 60); // 7 ngày

  private final String name;
  private final String path;
  private final String domain;
  private final boolean secure;
  private final boolean httpOnly;
  private final int maxAgeInSeconds;

  CookieConfig(String name, String path, String domain, boolean secure, boolean httpOnly, int maxAgeInSeconds) {
    this.name = name;
    this.path = path;
    this.domain = domain;
    this.secure = secure;
    this.httpOnly = httpOnly;
    this.maxAgeInSeconds = maxAgeInSeconds;
  }

  public Cookie createCookie(String value) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath(path);
    cookie.setDomain(domain);
    cookie.setSecure(secure);
    cookie.setHttpOnly(httpOnly);
    cookie.setMaxAge(maxAgeInSeconds); // Set maxAge mặc định là 7 ngày
    return cookie;
  }
}
