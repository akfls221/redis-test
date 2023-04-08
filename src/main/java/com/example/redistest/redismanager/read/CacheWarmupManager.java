package com.example.redistest.redismanager.read;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheWarmupManager {

    private final UserCacheManager userCacheManager;

    //@Scheduled(fixedDelay = 10000)
    public void userWithRedisTemplate() {
        userCacheManager.cache();
    }

}
