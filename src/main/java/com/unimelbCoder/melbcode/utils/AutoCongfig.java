package com.unimelbCoder.melbcode.utils;

import com.unimelbCoder.melbcode.cache.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class AutoCongfig {
    public AutoCongfig(RedisTemplate<String, String> redisTemplate){
        RedisClient.register(redisTemplate);
    }
}
