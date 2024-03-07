package com.unimelbCoder.melbcode.Service.Common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;

public class TokenBucketService {
    private static final String TOKEN_BUCKET_KEY = "token_bucket";
    private static final int MAX_POOL_SIZE = 1000;
    private static final int INIT_POOL_SIZE = 300;
    private static final int ADD_PER_MINUTE = 100;

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    public void initializeBucket() {
        ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
        valueOps.set(TOKEN_BUCKET_KEY, INIT_POOL_SIZE);
    }

    @Scheduled(fixedRate = 1000)
    public void autoAddToken(){
        ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
        Integer tokens = valueOps.get(TOKEN_BUCKET_KEY);
        if (tokens != null && tokens < MAX_POOL_SIZE){
            valueOps.set(TOKEN_BUCKET_KEY, Math.min(MAX_POOL_SIZE, tokens + ADD_PER_MINUTE));
        }
    }

    public boolean tryGetToken(){
        ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
        while(true){
            Integer tokens = valueOps.get(TOKEN_BUCKET_KEY);
            if(tokens != null && tokens > 0){
                // 获取token前需要先手动加锁
                if(valueOps.setIfAbsent(TOKEN_BUCKET_KEY + "_lock", 1)){
                    // 如果没获取到锁，重新进入循环自旋等待获取锁
                    try{
                        //再判断一次, 避免在上锁之前，另一个释放掉锁的线程获取完了最后一个令牌
                        Integer currentTokens = valueOps.get(TOKEN_BUCKET_KEY);
                        if(currentTokens != null && currentTokens > 0){
                            valueOps.set(TOKEN_BUCKET_KEY, currentTokens - 1);
                            return true;
                        }
                    }finally {
                        // 释放锁
                        redisTemplate.delete(TOKEN_BUCKET_KEY);
                    }
                }
            }else{
                // 如果令牌桶里面没有任何令牌了，才返回false
                return false;
            }
        }
    }
}
