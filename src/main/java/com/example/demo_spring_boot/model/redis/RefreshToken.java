package com.example.demo_spring_boot.model.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 604800) // TTL: 7 ngày
public class RefreshToken implements Serializable {

  @Id
  private String userId;

  private String token;

  private String clientIp; // Địa chỉ IP của thiết bị

  private String deviceInfo; // Thông tin thiết bị
}
