package com.example.demo_spring_boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
@ConditionalOnProperty(name = "spring.data.redis.enabled", havingValue = "true")
public class RedisConfig {
}
