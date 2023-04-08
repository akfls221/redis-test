package com.example.redistest.service.write;

import com.example.redistest.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class WriteBehind {

    private static final String USER_KEY = "user::%s";
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveUser(User user) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        redisTemplate.opsForValue().set(generateKey(user.getUserName()), user, Duration.ofSeconds(60));
    }

    private String generateKey(String userName) {
        return USER_KEY.formatted(userName);
    }
}
