package com.unimelbCoder.melbcode.cache;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;

public class RedisClient{

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisClient(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveSession(String sessionId, HttpSession session) {
        if (StringUtils.hasText(sessionId) && session != null) {
            String key = sessionId;
            redisTemplate.opsForValue().set(key, session);
            redisTemplate.expire(key, 30, TimeUnit.MINUTES); // Set expiration time (adjust as needed)
        }
    }

    public HttpSession getSession(String sessionId) {
        if (StringUtils.hasText(sessionId)) {
            String key = sessionId;
            return (HttpSession) redisTemplate.opsForValue().get(key);
        }
        return null;
    }

    public void deleteSession(String sessionId) {
        if (StringUtils.hasText(sessionId)) {
            String key = sessionId;
            redisTemplate.delete(key);
        }
    }
}