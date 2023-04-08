package com.example.redistest.service.read;

import com.example.redistest.domain.User;
import com.example.redistest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService2 {

    private final UserRepository userRepository;

    @Cacheable(key = "#userId", value = "userCache")
    public User testWithCacheableAnnotation(Long userId) {
        log.info("================ this is test with @Cacheable Annotation ================");
        return userRepository.findById(userId).get();
    }
}
