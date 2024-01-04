package com.unimelbCoder.melbcode.Service.Rank.Impl;

import com.unimelbCoder.melbcode.Service.Rank.UserRankService;
import com.unimelbCoder.melbcode.Service.Rank.model.ActivityRankBo;
import com.unimelbCoder.melbcode.Service.User.Impl.UserServiceImpl;
import com.unimelbCoder.melbcode.Service.User.UserService;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.RankItemDTO;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;
import com.unimelbCoder.melbcode.models.enums.ActivityRankTimeEnum;
import com.unimelbCoder.melbcode.utils.DateUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserRankServiceImpl implements UserRankService {
    private static final String ACTIVITY_SCORE_KEY = "activity_rank_";

    private static final Charset CODE = StandardCharsets.UTF_8;

    private static final String KEY_PREFIX = "melb_";

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserServiceImpl userService;

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
    public void addActivityScore(String userId, ActivityRankBo activityScore){
        System.out.println();

        if(userId == null){
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

        final String userActionKey = ACTIVITY_SCORE_KEY + DateUtils.format(DateTimeFormatter.ofPattern("yyyyMMdd"), System.currentTimeMillis());

        if(RedisClient.isMemberExists(userActionKey, userId)){
            if(score > 0){
                RedisClient.modifyScore(userActionKey, userId, score);
            }
        }else{
            System.out.println("successfully add the user to the rank list");
            RedisClient.addToSortedSet(userActionKey, userId, score);
        }

    }

//    @Override
//    public RankItemDTO queryRankInfo(String username, ActivityRankTimeEnum time, Integer rank, Integer score) {
//        RankItemDTO item = new RankItemDTO();
//        item.setUser(userService.queryUserInfo(username));
//        String rankKey = time == ActivityRankTimeEnum.DAY ? todayRankKey() : monthRankKey();
//        double score =
//
//        ImmutablePair<Integer, Double> rank = redisTemplate.zRankInfo(rankKey, String.valueOf(userId));
//        item.setRank(rank.getLeft());
//        item.setScore(rank.getRight().intValue());
//
//        return item;
//    }

    @Override
    public List<RankItemDTO> queryRankList(ActivityRankTimeEnum time, int size) {
        List<RankItemDTO> rankList = new LinkedList<>();
        String rankKey = time == ActivityRankTimeEnum.DAY ? todayRankKey() : monthRankKey();
        // 此处是返回所有的排行，不是按照size。
        Set<TypedTuple<String>> sortedMembers = RedisClient.getSortedSetMembers(rankKey, 0, -1);
        int i = 0;
        for(TypedTuple<String> tuple: sortedMembers){
            String member = tuple.getValue();
            Double score = tuple.getScore();
            SimpleUserInfoDTO spu = userService.queryUserInfo(member);
            rankList.add(new RankItemDTO().setUser(spu).setRank(i).setScore(score.intValue()));
        }
        return rankList;
    }


}
