package com.shantanu.journalApp.serviceTests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Disabled
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate  redisTemplate;

    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("email","shan@email.com");
        redisTemplate.opsForValue().set("city","Pune");
//        redisTemplate.opsForValue().set();

        String name = redisTemplate.opsForValue().get("name");
        Object salary = redisTemplate.opsForValue().get("salary");
        Object email = redisTemplate.opsForValue().get("email");
        String city = redisTemplate.opsForValue().get("city");
        System.out.println("name: "+ name + "salary: "+ salary +" email: "+ email + " city: " + city);
    }


}
