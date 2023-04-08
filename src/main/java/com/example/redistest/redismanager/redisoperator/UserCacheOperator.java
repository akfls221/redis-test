package com.example.redistest.redismanager.redisoperator;

import com.example.redistest.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCacheOperator {

    private static final String PREFIX_KEY = "user::%s";
    private static final Duration DEFAULT_TTL = Duration.ofSeconds(20);
    private final RedisTemplate<String, Object> redisTemplate;

    public Optional<User> get(Long key) {
        return Optional.ofNullable((User) redisTemplate.opsForValue().get(generateKey(key)));
    }

    public void set(User user) {
        redisTemplate.opsForValue().set(generateKey(user.getUserId()), user, DEFAULT_TTL);
    }

    private String generateKey(Long key) {
        return PREFIX_KEY.formatted(key);
    }

}
