package com.unimelbCoder.melbcode.cache;

import com.google.common.collect.Maps;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.utils.JsonUtils;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
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

    public static <T> byte[] valBytes(T val) {
        if (val instanceof String) {
            return ((String) val).getBytes(CODE);
        }
        else {
            return JsonUtils.toStr(val).getBytes(CODE);
        }
    }

    /**
     * 缓存键key的序列化生成
     * @param key
     * @return
     */
    public static byte[] keyBytes(String key) {
        nullCheck(key);
        key = KEY_PREFIX + key;
        return key.getBytes(CODE);
    }


    public static void delByKey(String key) {
        template.delete(key);
    }

    /**
     *
     * @param key 用日期作为key
     * @param member 其中不同的用户作为member
     * @param score 每个用户对应的得分
     */
    public static void addToSortedSet(String key, String member, double score) {
        ZSetOperations<String, String> zSetOps = template.opsForZSet();
        zSetOps.add(key, member, score);
    }

    public static Set<TypedTuple<String>> getSortedSetMembers(String key, long start, long end) {
        ZSetOperations<String, String> zSetOps = template.opsForZSet();
        return zSetOps.rangeWithScores(key, start, end);
    }

    public static void modifyScore(String key, String member, double scoreDelta) {
        ZSetOperations<String, String> zSetOps = template.opsForZSet();
        zSetOps.incrementScore(key, member, scoreDelta);
    }

    public static boolean isMemberExists(String key, String member) {
        ZSetOperations<String, String> zSetOps = template.opsForZSet();
        // Check if the member exists in the sorted set
        Double score = zSetOps.score(key, member);
        return score != null;
    }

    /**
     * 通过用户名检查redis缓存是否存在
     * @param username
     * @return
     */
    public static boolean isUserLogin(String username) {
        String checkToken = template.opsForValue().get(username);

        return checkToken != null;
    }

    /**
     * 用户的未读消息
     */
    public static void addMessage(String key, String message){
        template.opsForList().rightPush(key, message);
    }

    public static List<String> getMessages(String key) {
        // LRANGE to retrieve all messages from the list
        return template.opsForList().range(key, 0, -1);
    }

    public static Long hIncr(String key, String content, Integer cnt) {
        return template.execute((RedisCallback<Long>) con -> con.hIncrBy(keyBytes(key), valBytes(content), cnt));
    }

    public static <T> Map<String, T> hGetAll(String key, Class<T> cls) {
        Map<byte[], byte[]> records = template.execute((RedisCallback<Map<byte[], byte[]>>) con -> con.hGetAll(keyBytes(key)));
        if (records == null) {
            return Collections.emptyMap();
        }

        Map<String, T> result = Maps.newHashMapWithExpectedSize(records.size());
        for (Map.Entry<byte[], byte[]> entry : records.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }

            result.put(new String(entry.getKey()), toObj(entry.getValue(), cls));

        }

        return result;
    }

    private static <T> T toObj(byte[] ans, Class<T> clz) {
        if (ans == null) {
            return null;
        }

        if (clz == String.class) {
            return (T) new String(ans, CODE);
        }

        return JsonUtils.toObj(new String(ans, CODE), clz);
    }

    /**
     * 查询用户是否有投资记录
     */
    public static boolean isUserInvested(String userId){
        return false;
    }

    /**
     * 更新当天每个币种的价格
     * @param prices
     * @param tagName
     */
    public static void updateCoinPrice(Map<String, Double> prices, String tagName){
        HashOperations<String, String, Double> hashOps = template.opsForHash();
        hashOps.putAll(tagName, prices);
    }

    public static Map<String, Double> getCoinPrices(String tagName){
        HashOperations<String, String, Double> hashOps = template.opsForHash();
        Map<String, Double> prices = hashOps.entries(tagName);
        return new HashMap<>(prices);
    }
}