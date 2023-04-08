package com.example.redistest.config;

import com.example.redistest.config.properties.RedisProperties;
import com.example.redistest.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setPassword(redisProperties.password());
        redisStandaloneConfiguration.setHostName(redisProperties.host());
        redisStandaloneConfiguration.setPort(redisProperties.port());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

//    @Bean //RedisConnectionFactory라는 객체에 Redis 접속 정보를 입력해 캐싱 기능을 명시한 곳에 사용할 수 있도록 설정 정보를 담은 객체이다.
//    public RedisConnectionFactory redisConnectionFactory2() {
//        return new LettuceConnectionFactory(redisProperties.host(), redisProperties.port());
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    /**
     * RedisCacheManager를 활용한 RedisCacheManager bean 등록(여러개의 cacheManager를 체이닝 하여 등록가능)
     * @return RedisCacheManagerBuilderCustomizer
     *
     * 참조 : https://www.baeldung.com/spring-boot-redis-cache
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration(RedisProperties.USER_CACHE,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(RedisProperties.USER_CACHE_TTL)
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(User.class))))
                .withCacheConfiguration(RedisProperties.CUSTOM_CACHE,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(RedisProperties.CUSTOM_CACHE_TTL));
    }
}
