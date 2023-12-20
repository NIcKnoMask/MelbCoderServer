package com.unimelbCoder.melbcode.Service.Rank.Impl;

import com.unimelbCoder.melbcode.Service.Rank.UserRankService;
import com.unimelbCoder.melbcode.Service.Rank.model.ActivityRankBo;
import com.unimelbCoder.melbcode.Service.User.UserService;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.RankItemDTO;
import com.unimelbCoder.melbcode.models.enums.ActivityRankTimeEnum;
import com.unimelbCoder.melbcode.utils.DateUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserRankServiceImpl implements UserRankService {
    private static final String ACTIVITY_SCORE_KEY = "activity_rank_";

    private static final Charset CODE = StandardCharsets.UTF_8;

    private static final String KEY_PREFIX = "melb_";

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 当天活跃度排行榜
     *
     * @return 当天排行榜key
     */
    private String todayRankKey() {
        return ACTIVITY_SCORE_KEY + DateUtils.format(DateTimeFormatter.ofPattern("yyyyMMdd"), System.currentTimeMillis());
    }

    /**
     * 本月排行榜
     *
     * @return 月度排行榜key
     */
    private String monthRankKey() {
        return ACTIVITY_SCORE_KEY + DateUtils.format(DateTimeFormatter.ofPattern("yyyyMM"), System.currentTimeMillis());
    }

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

    @Override
    public void addActivityScore(String username, ActivityRankBo activityScore){

    }

    @Override
    public RankItemDTO queryRankInfo(String username, ActivityRankTimeEnum time) {
        RankItemDTO item = new RankItemDTO();
        item.setUser(userService.queryUserInfo(username));
        String rankKey = time == ActivityRankTimeEnum.DAY ? todayRankKey() : monthRankKey();
        double score = redisTemplate.execute(new RedisCallback<Double>() {
            @Override
            public Double doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.zScore(keyBytes(rankKey), keyBytes(username));
            }
        });

//        ImmutablePair<Integer, Double> rank = redisTemplate.zRankInfo(rankKey, String.valueOf(userId));
//        item.setRank(rank.getLeft());
//        item.setScore(rank.getRight().intValue());

        return item;
    }

//    @Override
//    public List<RankItemDTO> queryRankList(ActivityRankTimeEnum time, int size) {
//    }


}
