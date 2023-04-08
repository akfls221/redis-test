package com.example.redistest.service.read;

import com.example.redistest.domain.User;
import com.example.redistest.redismanager.redisoperator.UserCacheOperator;
import com.example.redistest.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements InitializingBean {
    private final UserCacheOperator userCacheOperator;
    private final UserRepository userRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        User user = new User("testUser", 30);
        userRepository.save(user);
    }

    public User testWithRedisTemplate(Long userId) {
        log.info("================this is testWithRedisTemplate================");
        return userCacheOperator.get(userId)
                .orElseGet(() -> {
                    User findUser = userRepository.findById(userId)
                            .orElseThrow(() -> new NoSuchElementException("Entity Not Found"));
                    userCacheOperator.set(findUser);
                    return findUser;
                });
    }
}
