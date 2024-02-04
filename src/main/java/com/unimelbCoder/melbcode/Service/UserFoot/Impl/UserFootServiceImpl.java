package com.unimelbCoder.melbcode.Service.UserFoot.Impl;

import com.unimelbCoder.melbcode.Service.UserFoot.UserFootService;
import com.unimelbCoder.melbcode.bean.Comment;
import com.unimelbCoder.melbcode.bean.UserFoot;
import com.unimelbCoder.melbcode.models.dao.UserFootDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserFootServiceImpl implements UserFootService {

    // TODO: 根据用户表单更新后合并修改

    @Autowired
    private UserFootDao userFootDao;

    @Override
    public UserFoot saveOrUpdateUserFoot(Integer documentType, Long documentId, String authorId, String userId, Integer operateType) {
        // 查询是否有足迹，有的话就更新，没有则新增
        UserFoot userFoot = userFootDao.getUserFootByDocumentAndUserId(userId, documentId, documentType);
        if (userFoot == null) {
            userFoot = new UserFoot();
            userFoot.setUserId(userId);
            userFoot.setDocumentId(documentId);
            userFoot.setDocumentType(documentType);
            userFoot.setDocumentUserId(authorId);
            userFootDao.createUserFoot(userId, documentId, documentType, authorId);
        }
        else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            userFootDao.updateUserFootById(userFoot, timestamp);
        }

        return userFoot;
    }

    @Override
    public void saveCommentFoot(Comment comment, String articleAuthor, String parentCommentAuthor) {

        // 保存文章对应的评论足迹
        saveOrUpdateUserFoot(1, comment.getArticle_id(), articleAuthor, comment.getUser_id(), 6);

        // 如果评论是子评论，则需要找到父评论的记录，然后设置为已读
        if (comment.getParent_comment_id() != null && comment.getParent_comment_id() != 0) {
            saveOrUpdateUserFoot(2, comment.getParent_comment_id(), parentCommentAuthor, comment.getUser_id(), 6);
        }

    }

    @Override
    public void removeCommentFoot(Comment comment, String articleAuthor, String parentCommentAuthor) {

    }

    @Override
    public UserFoot queryUserFoot(Long documentId, Integer type, Long userId) {
        return null;
    }
}
