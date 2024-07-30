package com.unimelbCoder.melbcode.bean;

import java.sql.Timestamp;

public class ArticleDetail {

    private int id;
    private int article_id;
    private int version;
    private String content;
    private int ref_loc;
    private int deleted;
    private Timestamp create_time;
    private Timestamp update_time;

    public ArticleDetail(int id, int article_id, int version, String content, int ref_loc, int deleted,
                         Timestamp create_time, Timestamp update_time) {
        this.id = id;
        this.article_id = article_id;
        this.version = version;
        this.content = content;
        this.ref_loc = ref_loc;
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

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRef_loc() {
        return ref_loc;
    }

    public void setRef_loc(int ref_loc) {
        this.ref_loc = ref_loc;
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
                ", article id='" + this.article_id + '\'' +
                ", version='" + this.version + '\'' +
                ", content='" + this.content + '\'' +
                ", ref_loc='" + this.ref_loc + '\'' +
                ", deleted=" + this.deleted + '\'' +
                ", create time=" + this.create_time + '\'' +
                ", update time=" + this.update_time + '\'' +
                '}';
    }
}
