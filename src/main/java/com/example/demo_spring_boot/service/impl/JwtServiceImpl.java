package com.example.demo_spring_boot.service.impl;

import com.example.demo_spring_boot.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
  private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

  @Value("${jwt.secret-key}")
  private String secretKey;

  @Value("${jwt.access-token.expiration}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token.expiration}")
  private long refreshTokenExpiration;

  @Value("${jwt.issuer}")
  private String issuer;

  private Key signingKey;

  @PostConstruct
  public void init() {
    this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public String generateAccessToken(UserDetails userDetails) {
    return buildToken(userDetails.getUsername(), accessTokenExpiration);
  }

  @Override
  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(userDetails.getUsername(), refreshTokenExpiration);
  }

  private String buildToken(String subject, long expiration) {
    return Jwts.builder()
        .setSubject(subject)
        .setIssuer(issuer)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public boolean validateToken(String token, UserDetails userDetails) {
    try {
      final String username = extractUsername(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    } catch (JwtException ex) {
      logger.error("JWT validation failed: {}", ex.getMessage());
      return false;
    }
  }

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    try {
      final Claims claims = Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
      return claimsResolver.apply(claims);
    } catch (JwtException ex) {
      logger.error("Error extracting claims: {}", ex.getMessage());
      throw ex;
    }
  }

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  public boolean validateTokenStructure(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
      return true;
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token: {}", ex.getMessage());
    } catch (ExpiredJwtException ex) {
      logger.error("JWT token is expired: {}", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      logger.error("JWT token is unsupported: {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty: {}", ex.getMessage());
    }
    return false;
  }
}