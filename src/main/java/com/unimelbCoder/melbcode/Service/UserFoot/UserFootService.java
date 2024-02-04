package com.unimelbCoder.melbcode.Service.UserFoot;

import com.unimelbCoder.melbcode.bean.Comment;
import com.unimelbCoder.melbcode.bean.UserFoot;

public interface UserFootService {

    // TODO: 根据用户表单更新后合并修改

    /**
     * 保存或更新状态信息
     *
     * @param documentType    文档类型：博文 + 评论
     * @param documentId      文档id
     * @param authorId        作者
     * @param userId          操作人
     * @param operateType 操作类型：点赞，评论，收藏等
     * @return
     */
    UserFoot saveOrUpdateUserFoot(Integer documentType, Long documentId, String authorId, String userId, Integer operateType);

    /**
     * 保存评论足迹
     * 1. 用户文章记录上，设置为已评论
     * 2. 若改评论为回复别人的评论，则针对父评论设置为已评论
     *
     * @param comment             保存评论入参
     * @param articleAuthor       文章作者
     * @param parentCommentAuthor 父评论作者
     */
    void saveCommentFoot(Comment comment, String articleAuthor, String parentCommentAuthor);

    /**
     * 删除评论足迹
     *
     * @param comment             保存评论入参
     * @param articleAuthor       文章作者
     * @param parentCommentAuthor 父评论作者
     */
    void removeCommentFoot(Comment comment, String articleAuthor, String parentCommentAuthor);

    /**
     * 查询用户记录，用于判断是否点过赞、是否评论、是否收藏过
     *
     * @param documentId
     * @param type
     * @param userId
     * @return
     */
    UserFoot queryUserFoot(Long documentId, Integer type, Long userId);

}
