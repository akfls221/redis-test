package com.example.redistest.service.write;

import com.example.redistest.domain.User;
import com.example.redistest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class WriteThrough implements InitializingBean {

    private static final String USER_KEY = "user::%s";
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
    }

    public void writeThroughSave(User user) {
        redisTemplate.opsForValue().set(generateKey(user.getUserName()), user, Duration.ofSeconds(60));
        userRepository.save(user);

    }

    private String generateKey(String userName) {
        return USER_KEY.formatted(userName);
    }
}
