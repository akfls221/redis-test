package com.example.redistest.redismanager.write;

import com.example.redistest.domain.User;
import com.example.redistest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.results.graph.Initializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class UserSaveManager implements WriteManager, InitializingBean {

    private static final String USER_KEY = "user::*";
    private final RedisTemplate<String, Object> redisTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
    }

    @Override
    public void saveAll() {
        Set<String> userKey = redisTemplate.keys(USER_KEY);

        List<User> users = new ArrayList<>();
        for (String key : userKey) {
            User user = (User) redisTemplate.opsForValue().get(key);
            users.add(user);
        }
        executeBatchQuery(users);
    }

    private int[] executeBatchQuery(List<User> users) {
        log.info("================start user save batch query================");
        String sql = "INSERT INTO USERS (USER_AGE, USER_NAME)  VALUES (?, ?)";

        return jdbcTemplate.batchUpdate(
                sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, users.get(i).getUserAge());
                        ps.setString(2, users.get(i).getUserName());
                    }

                    @Override
                    public int getBatchSize() {
                        return users.size();
                    }
                }
        );
    }
}
