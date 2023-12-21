package com.unimelbCoder.melbcode.cache;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisClient{

    private static RedisTemplate<String, String> template;

    public static void register(RedisTemplate<String, String> template){
        RedisClient.template = template;
    }


    private static final Charset CODE = StandardCharsets.UTF_8;

    private static final String KEY_PREFIX = "melb_";

    public static void nullCheck(Object... args) {
        for (Object obj : args) {
            if (obj == null) {
                throw new IllegalArgumentException("redis argument can not be null!");
            }
        }
    }

    public static byte[] keyBytes(String key) {
        nullCheck(key);
        key = KEY_PREFIX + key;
        return key.getBytes(CODE);
    }


    public static void del(String key) {
        template.execute((RedisCallback<Long>) con -> con.del(keyBytes(key)));
    }

    /**
     *
     * @param key 用日期作为key
     * @param member 其中不同的用户作为member
     * @param score 每个用户对应的得分
     */
    public static void addToSortedSet(String key, String member, double score) {
        System.out.println("invoke zset add function");
        System.out.println(template);
        ZSetOperations<String, String> zSetOps = template.opsForZSet();
        zSetOps.add(key, member, score);
    }

    public static Set<String> getSortedSetMembers(String key, long start, long end) {
        ZSetOperations<String, String> zSetOps = template.opsForZSet();
        return zSetOps.range(key, start, end);
    }

    public static void modifyScore(String key, String member, double scoreDelta) {
        System.out.println(template);

        ZSetOperations<String, String> zSetOps = template.opsForZSet();
        zSetOps.incrementScore(key, member, scoreDelta);
    }

    public static boolean isMemberExists(String key, String member) {
        System.out.println(template);
        ZSetOperations<String, String> zSetOps = template.opsForZSet();
        // Check if the member exists in the sorted set
        Double score = zSetOps.score(key, member);
        return score != null;
    }
}