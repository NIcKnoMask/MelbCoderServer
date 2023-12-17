package com.unimelbCoder.melbcode.Controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class RedisController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/test/get/{key}")
    public Object get(@PathVariable("key") String key){
        return redisTemplate.opsForValue().get(key);
    }

    @PostMapping("/test/set/{key}/{value}")
    public Object set(@PathVariable("key") String key, @PathVariable("value") String value){
        redisTemplate.opsForValue().set(key, value);
        return "set success";
    }
}
