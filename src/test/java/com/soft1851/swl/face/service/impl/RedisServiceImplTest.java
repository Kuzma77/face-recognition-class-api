package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisServiceImplTest {
    @Resource
    private RedisService redisService;
    @Test
    void set() {
        redisService.set("111","123456789",1L);
    }
}