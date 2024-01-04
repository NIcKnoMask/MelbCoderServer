package com.unimelbCoder.melbcode.bean;

import java.sql.Timestamp;

public class Comment {

    private Integer article_id;

    private String user_id;

    private String content;

    private Integer top_comment_id;

    private Integer parent_comment_id;

    private Integer deleted;

    public Comment(Integer article_id, String user_id, String content, Integer top_comment_id, Integer parent_comment_id, Integer deleted) {
        this.article_id = article_id;
        this.user_id = user_id;
        this.content = content;
        this.top_comment_id = top_comment_id;
        this.parent_comment_id = parent_comment_id;
        this.deleted = deleted;
    }

    public Integer getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Integer article_id) {
        this.article_id = article_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTop_comment_id() {
        return top_comment_id;
    }

    public void setTop_comment_id(Integer top_comment_id) {
        this.top_comment_id = top_comment_id;
    }

    public Integer getParent_comment_id() {
        return parent_comment_id;
    }

    public void setParent_comment_id(Integer parent_comment_id) {
        this.parent_comment_id = parent_comment_id;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
