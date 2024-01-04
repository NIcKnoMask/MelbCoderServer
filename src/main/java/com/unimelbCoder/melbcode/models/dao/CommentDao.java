package com.unimelbCoder.melbcode.models.dao;

import com.unimelbCoder.melbcode.bean.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao {

    public Comment getCommentById(@Param("id") Integer id);

    public Comment getCommentByIdxArticle(@Param("article_id") Integer article_id);

    public Comment getCommentByIdxUserId(@Param("user_id") Integer user_id);

    /**
     * 获取文章评论列表
     * @param article_id
     * @return
     */
    public List<Comment> getTopCommentList(@Param("article_id") Integer article_id);

    public List<Comment> getSubCommentList(@Param("article_id") Integer article_id,
                                           @Param("top_comment_id") Integer top_comment_id);

    /**
     * 查看文章有效评论数
     * @param article_id
     * @return
     */
    public Integer getValidCommentCount(@Param("article_id") Integer article_id);

    public void createComment(@Param("article_id") Integer article_id,
                              @Param("user_id") String user_id,
                              @Param("content") String content,
                              @Param("top_comment_id") Integer top_comment_id,
                              @Param("parent_comment_id") Integer parent_comment_id);

}
