package com.unimelbCoder.melbcode.Service.Rank.Impl;

import com.unimelbCoder.melbcode.Service.Rank.UserRankService;
import com.unimelbCoder.melbcode.Service.Rank.model.ActivityRankBo;
import com.unimelbCoder.melbcode.Service.User.Impl.UserServiceImpl;
import com.unimelbCoder.melbcode.Service.User.UserService;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.RankItemDTO;
import com.unimelbCoder.melbcode.models.enums.ActivityRankTimeEnum;
import com.unimelbCoder.melbcode.utils.DateUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Service
public class UserRankServiceImpl implements UserRankService {
    private static final String ACTIVITY_SCORE_KEY = "activity_rank_";

    private static final Charset CODE = StandardCharsets.UTF_8;

    private static final String KEY_PREFIX = "melb_";

    @Autowired
    private UserDao userDao;

    private UserServiceImpl userService = new UserServiceImpl();

//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;


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
        System.out.println();

        if(username == null){
            return;
        }

        String field;
        int score = 0;
        if (activityScore.getPath() != null) {
            field = "path_" + activityScore.getPath();
            score = 1;
        } else if (activityScore.getArticleId() != null) {
            field = activityScore.getArticleId() + "_";
            if (activityScore.getPraise() != null) {
                field += "praise";
                score = BooleanUtils.isTrue(activityScore.getPraise()) ? 2 : -2;
            } else if (activityScore.getCollect() != null) {
                field += "collect";
                score = BooleanUtils.isTrue(activityScore.getCollect()) ? 2 : -2;
            } else if (activityScore.getRate() != null) {
                // 评论回复
                field += "rate";
                score = BooleanUtils.isTrue(activityScore.getRate()) ? 3 : -3;
            } else if (BooleanUtils.isTrue(activityScore.getPublishArticle())) {
                // 发布文章
                field += "publish";
                score += 10;
            }
        } else if (activityScore.getFollowedUserId() != null) {
            field = activityScore.getFollowedUserId() + "_follow";
            score = BooleanUtils.isTrue(activityScore.getFollow()) ? 2 : -2;
        }  else if (activityScore.getLogin() != null){
            field = activityScore.getFollowedUserId() + "_login";
            score += 2;
        } else {
            return;
        }

        final String userActionKey = ACTIVITY_SCORE_KEY + username + DateUtils.format(DateTimeFormatter.ofPattern("yyyyMMdd"), System.currentTimeMillis());

        if(RedisClient.isMemberExists(userActionKey, username)){
            if(score > 0){
                RedisClient.modifyScore(userActionKey, username, score);
            }
        }else{
            System.out.println("successfully add the user to the rank list");
            RedisClient.addToSortedSet(userActionKey, username, score);
        }

    }

    @Override
    public RankItemDTO queryRankInfo(String username, ActivityRankTimeEnum time) {
        RankItemDTO item = new RankItemDTO();
//        item.setUser(userService.queryUserInfo(username));
//        String rankKey = time == ActivityRankTimeEnum.DAY ? todayRankKey() : monthRankKey();
//        double score = redisTemplate.execute(new RedisCallback<Double>() {
//            @Override
//            public Double doInRedis(RedisConnection connection) throws DataAccessException {
//                return connection.z;
//            }
//        });

//        ImmutablePair<Integer, Double> rank = redisTemplate.zRankInfo(rankKey, String.valueOf(userId));
//        item.setRank(rank.getLeft());
//        item.setScore(rank.getRight().intValue());

        return item;
    }

//    @Override
//    public List<RankItemDTO> queryRankList(ActivityRankTimeEnum time, int size) {
//    }


}
