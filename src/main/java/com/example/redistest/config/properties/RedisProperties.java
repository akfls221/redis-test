package com.example.redistest.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "test.data.redis")
public record RedisProperties(
        String host,
        int port,
        String password
) {
    public static final String USER_CACHE = "userCache";
    public static final String CUSTOM_CACHE = "customCache";
    public static final Duration USER_CACHE_TTL = Duration.ofSeconds(10);
    public static final Duration CUSTOM_CACHE_TTL = Duration.ofSeconds(5);
}
