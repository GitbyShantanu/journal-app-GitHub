package com.shantanu.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * Redis se data fetch karke Java object mein convert
     * @param key - Redis key(string)
     * @param targetEntityClass - Target Response(output) class type(POJO)
     * @return Converted object or null
     */
    public <T> T get(String key, Class<T> targetEntityClass) {
        try {
            Object o = stringRedisTemplate.opsForValue().get(key);
            if (o == null) {
                log.info("Cache miss for {}, fetching from API", key);
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(o.toString(), targetEntityClass);

        } catch (Exception e) {
            log.error("Error while fetching from Redis: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Java object ko Redis mein save with TTL
     * @param key - Redis key
     * @param value - Any Java object
     * @param ttl - Time to live(auto delete) in seconds
     */
    public void set(String key, Object value, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, jsonBody, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Failed to save key '{}' in Redis | Reason: {}", key, e.getMessage());
        }
    }

}