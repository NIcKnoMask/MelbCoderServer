package com.unimelbCoder.melbcode.Service.Statistics.Impl;

import com.unimelbCoder.melbcode.Service.Statistics.CountConstants;
import com.unimelbCoder.melbcode.Service.Statistics.CountService;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dto.article.ArticleFootCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CountServiceImpl implements CountService {

    @Override
    public ArticleFootCountDTO queryArticleStatisticInfo(Integer articleId) {
        Map<String, Integer> ans = RedisClient.hGetAll(CountConstants.ARTICLE_STATISTIC_INFO + articleId, Integer.class);

        ArticleFootCountDTO countDTO = new ArticleFootCountDTO();
        countDTO.setPraiseCount(ans.getOrDefault(CountConstants.PRAISE_COUNT, 0));
        countDTO.setCommentCount(ans.getOrDefault(CountConstants.COMMENT_COUNT, 0));
        countDTO.setCollectionCount(ans.getOrDefault(CountConstants.COLLECTION_COUNT, 0));
        countDTO.setReadCount(ans.getOrDefault(CountConstants.READ_COUNT, 0));

        return countDTO;
    }

    // TODO: 阅读计数需要实现
    @Override
    public void incrArticleReadCount(String authorUserId, Integer articleId) {

    }
}
