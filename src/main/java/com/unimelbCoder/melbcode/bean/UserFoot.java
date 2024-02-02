package com.unimelbCoder.melbcode.bean;

import java.sql.Timestamp;

public class UserFoot {

    // TODO: 根据用户表单更新后合并修改

    /*
    主键ID
     */
    private Long id;

    /*
    用户ID
     */
    private Long userId;

    /*
    文档ID （文章/评论）
     */
    private Long documentId;

    /*
    1-文章，2-评论
     */
    private Integer documentType;

    /*
    发布文档的用户ID
     */
    private Long documentUserId;

    /*
    收藏状态：0-未收藏，1-已收藏，2-取消收藏
     */
    private Integer collectionStat;

    /*
    阅读状态：0-未读，1-已读
     */
    private Integer readStat;

    /*
    评论状态：0-未评论，1-已评论，2-删除评论
     */
    private Integer commentStat;

    /*
    点赞状态：0-未点赞，1-已点赞，2-取消点赞
     */
    private Integer praiseStat;

    /*
    创建时间
     */
    private Timestamp createTime;

    /*
    更新时间
     */
    private Timestamp updateTime;

    public UserFoot(Long id, Long userId, Long documentId, Integer documentType, Long documentUserId,
                    Integer collectionStat, Integer readStat, Integer commentStat, Integer praiseStat,
                    Timestamp createTime, Timestamp updateTime) {
        this.id = id;
        this.userId = userId;
        this.documentId = documentId;
        this.documentType = documentType;
        this.documentUserId = documentUserId;
        this.collectionStat = collectionStat;
        this.readStat = readStat;
        this.commentStat = commentStat;
        this.praiseStat = praiseStat;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserFoot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public Long getDocumentUserId() {
        return documentUserId;
    }

    public void setDocumentUserId(Long documentUserId) {
        this.documentUserId = documentUserId;
    }

    public Integer getCollectionStat() {
        return collectionStat;
    }

    public void setCollectionStat(Integer collectionStat) {
        this.collectionStat = collectionStat;
    }

    public Integer getReadStat() {
        return readStat;
    }

    public void setReadStat(Integer readStat) {
        this.readStat = readStat;
    }

    public Integer getCommentStat() {
        return commentStat;
    }

    public void setCommentStat(Integer commentStat) {
        this.commentStat = commentStat;
    }

    public Integer getPraiseStat() {
        return praiseStat;
    }

    public void setPraiseStat(Integer praiseStat) {
        this.praiseStat = praiseStat;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
