package com.unimelbCoder.melbcode.Service.Statistics;

import com.unimelbCoder.melbcode.models.dto.article.ArticleFootCountDTO;

public interface CountService {

    /**
     * 查询文章相关的统计信息
     *
     * @param articleId
     * @return 返回文章的 收藏、点赞、评论、阅读数
     */
    ArticleFootCountDTO queryArticleStatisticInfo(Integer articleId);

    /**
     * 文章计数+1
     *
     * @param authorUserId 作者
     * @param articleId    文章
     * @return 计数器
     */
    void incrArticleReadCount(String authorUserId, Integer articleId);

}
