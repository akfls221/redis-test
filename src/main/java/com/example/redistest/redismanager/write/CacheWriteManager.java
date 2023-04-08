package com.example.redistest.redismanager.write;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheWriteManager {

    private final UserSaveManager userSaveManager;

//    @Scheduled(fixedDelay = 60000)
    public void userWithRedisTemplate() {
        log.info("start batch insert USer");
        userSaveManager.saveAll();
    }
}
