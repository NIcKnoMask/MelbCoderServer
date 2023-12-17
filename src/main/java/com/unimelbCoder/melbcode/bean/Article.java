package com.unimelbCoder.melbcode.bean;

import java.sql.Timestamp;

public class Article {

    private int id;
    private int user_id;
    private int article_type;
    private String title;
    private String short_title;
    private int category_id;
    private int public_status;
    private int deleted;
    private Timestamp create_time;
    private Timestamp update_time;

    public Article(int id, int user_id, int article_type, String title, String short_title, int category_id,
                   int public_status, int deleted, Timestamp create_time, Timestamp update_time) {
        this.id = id;
        this.user_id = user_id;
        this.article_type = article_type;
        this.title = title;
        this.short_title = short_title;
        this.category_id = category_id;
        this.public_status = public_status;
        this.deleted = deleted;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getArticle_type() {
        return article_type;
    }

    public void setArticle_type(int article_type) {
        this.article_type = article_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShort_title() {
        return short_title;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getPublic_status() {
        return public_status;
    }

    public void setPublic_status(int public_status) {
        this.public_status = public_status;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.id +
                ", user id='" + this.user_id + '\'' +
                ", article type='" + this.article_type + '\'' +
                ", title='" + this.title + '\'' +
                ", short title='" + this.short_title + '\'' +
                ", category id=" + this.category_id +
                ", public status=" + this.public_status + '\'' +
                ", deleted=" + this.deleted + '\'' +
                ", create time=" + this.create_time + '\'' +
                ", update time=" + this.update_time + '\'' +
                '}';
    }
}
