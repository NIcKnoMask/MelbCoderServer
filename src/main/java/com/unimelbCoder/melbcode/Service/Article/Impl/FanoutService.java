package com.unimelbCoder.melbcode.Service.Article.Impl;

import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.UserRelationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FanoutService {
    @Autowired
    ArticleDao articleDao;

    @Autowired
    UserRelationDao userRelation;

    /**
     * 用户发送了文章后，会把文章推送到他的粉丝的timeline中
     * @param userId userid
     */
    public void articleToTimeline(int articleId, String userId){
        System.out.println("now fanouting");
        List<String> fans = userRelation.getFans(userId);
        System.out.println(articleId);
        System.out.println(userId);
        // 调用消息队列的接口 异步完成对所有粉丝timeline的更新
        long currentTimeMillis = System.currentTimeMillis();
        // Convert long to double
        double score = (double) currentTimeMillis;
        // 给每个粉丝的timeline中都推送这条
        for(String fanId: fans){
            RedisClient.addToSortedSet("timeline_" + fanId, Integer.toString(articleId), score);
        }
    }
}
