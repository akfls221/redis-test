package com.example.redistest.redismanager.read;

import com.example.redistest.domain.User;
import com.example.redistest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCacheManager implements CacheManager {

    private final UserRepository userRepository;
    private final RedisOperations<String, Object> redisTemplate;

    @Override
    public void cache() {
        log.info("cacheWithRedisTemplate start");
        List<User> users = userRepository.findAll();
        for (User user : users) {
            redisTemplate.opsForValue().set(user.getUserId().toString(), user, Duration.ofSeconds(5));
        }
    }
}
