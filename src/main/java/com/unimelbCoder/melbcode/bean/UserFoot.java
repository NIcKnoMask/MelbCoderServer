package com.unimelbCoder.melbcode.bean;

import java.sql.Timestamp;

public class UserFoot {

    /*
    主键ID
     */
    private Long id;

    /*
    用户ID
     */
    private String user_id;

    /*
    文档ID （文章/评论）
     */
    private Long document_id;

    /*
    1-文章，2-评论
     */
    private Integer document_type;

    /*
    发布文档的用户ID
     */
    private String document_user_id;

    /*
    收藏状态：0-未收藏，1-已收藏，2-取消收藏
     */
    private Integer collection_stat;

    /*
    阅读状态：0-未读，1-已读
     */
    private Integer read_stat;

    /*
    评论状态：0-未评论，1-已评论，2-删除评论
     */
    private Integer comment_stat;

    /*
    点赞状态：0-未点赞，1-已点赞，2-取消点赞
     */
    private Integer praise_stat;

    /*
    创建时间
     */
    private Timestamp create_time;

    /*
    更新时间
     */
    private Timestamp update_time;

    public UserFoot(Long id, String userId, Long documentId, Integer documentType, String documentUserId,
                    Integer collectionStat, Integer readStat, Integer commentStat, Integer praiseStat,
                    Timestamp createTime, Timestamp updateTime) {
        this.id = id;
        this.user_id = userId;
        this.document_id = documentId;
        this.document_type = documentType;
        this.document_user_id = documentUserId;
        this.collection_stat = collectionStat;
        this.read_stat = readStat;
        this.comment_stat = commentStat;
        this.praise_stat = praiseStat;
        this.create_time = createTime;
        this.update_time = updateTime;
    }

    public UserFoot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public Long getDocumentId() {
        return document_id;
    }

    public void setDocumentId(Long documentId) {
        this.document_id = documentId;
    }

    public Integer getDocumentType() {
        return document_type;
    }

    public void setDocumentType(Integer documentType) {
        this.document_type = documentType;
    }

    public String getDocumentUserId() {
        return document_user_id;
    }

    public void setDocumentUserId(String documentUserId) {
        this.document_user_id = documentUserId;
    }

    public Integer getCollectionStat() {
        return collection_stat;
    }

    public void setCollectionStat(Integer collectionStat) {
        this.collection_stat = collectionStat;
    }

    public Integer getReadStat() {
        return read_stat;
    }

    public void setReadStat(Integer readStat) {
        this.read_stat = readStat;
    }

    public Integer getCommentStat() {
        return comment_stat;
    }

    public void setCommentStat(Integer commentStat) {
        this.comment_stat = commentStat;
    }

    public Integer getPraiseStat() {
        return praise_stat;
    }

    public void setPraiseStat(Integer praiseStat) {
        this.praise_stat = praiseStat;
    }

    public Timestamp getCreateTime() {
        return create_time;
    }

    public void setCreateTime(Timestamp createTime) {
        this.create_time = createTime;
    }

    public Timestamp getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.update_time = updateTime;
    }
}
