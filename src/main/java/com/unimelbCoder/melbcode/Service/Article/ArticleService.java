package com.unimelbCoder.melbcode.Service.Article;

import com.unimelbCoder.melbcode.models.dto.article.ArticleDTO;

public interface ArticleService {

    /**
     * 查询文章详情，包括正文内容，分类、标签等信息
     *
     * @param articleId
     * @return
     */
    ArticleDTO queryDetailArticleInfo(Integer articleId);

    /**
     * 查询文章所有的关联信息，正文，分类，标签，阅读计数+1，当前登录用户是否点赞、评论过
     *
     * @param articleId   文章id
     * @param currentUser 当前查看的用户ID
     * @return
     */
    ArticleDTO queryFullArticleInfo(Integer articleId, String currentUser);

}
