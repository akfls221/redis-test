package com.example.redistest.controller;

import com.example.redistest.domain.User;
import com.example.redistest.service.read.UserService;
import com.example.redistest.service.read.UserService2;
import com.example.redistest.service.write.WriteBehind;
import com.example.redistest.service.write.WriteThrough;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserService2 userService2;
    private final WriteBehind writeBehind;
    private final WriteThrough writeThrough;

    @GetMapping("test1/{userId}")
    public User test(@PathVariable Long userId) {
        return userService.testWithRedisTemplate(userId);
    }

    @GetMapping("test2/{userId}")
    public User test2(@PathVariable Long userId) {
        return userService2.testWithCacheableAnnotation(userId);
    }

    @PostMapping("test/user")
    public void writeTest(@RequestBody User user) {
        writeBehind.saveUser(user);
    }

    @PostMapping("test2/user")
    public void writeThroughTest(@RequestBody User user) {
        writeThrough.writeThroughSave(user);
    }
}
