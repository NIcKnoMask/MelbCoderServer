package com.unimelbCoder.melbcode.bean;

import java.sql.Timestamp;

public class Comment {

    private Long id;

    private Long article_id;

    private String user_id;

    private String content;

    private Long top_comment_id;

    private Long parent_comment_id;

    private Integer deleted;

    private Timestamp create_time;

    private Timestamp update_time;

    public Comment(Long id, Long article_id, String user_id, String content, Long top_comment_id,
                   Long parent_comment_id, Integer deleted, Timestamp create_time, Timestamp update_time) {
        this.id = id;
        this.article_id = article_id;
        this.user_id = user_id;
        this.content = content;
        this.top_comment_id = top_comment_id;
        this.parent_comment_id = parent_comment_id;
        this.deleted = deleted;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public Comment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Long article_id) {
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

    public Long getTop_comment_id() {
        return top_comment_id;
    }

    public void setTop_comment_id(Long top_comment_id) {
        this.top_comment_id = top_comment_id;
    }

    public Long getParent_comment_id() {
        return parent_comment_id;
    }

    public void setParent_comment_id(Long parent_comment_id) {
        this.parent_comment_id = parent_comment_id;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }
}
