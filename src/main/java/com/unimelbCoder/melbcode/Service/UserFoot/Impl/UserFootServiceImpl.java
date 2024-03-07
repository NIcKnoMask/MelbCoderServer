package com.unimelbCoder.melbcode.Service.UserFoot.Impl;

import com.unimelbCoder.melbcode.Service.UserFoot.UserFootService;
import com.unimelbCoder.melbcode.bean.Comment;
import com.unimelbCoder.melbcode.bean.UserFoot;
import com.unimelbCoder.melbcode.models.dao.UserFootDao;
import com.unimelbCoder.melbcode.models.enums.DocumentTypeEnum;
import com.unimelbCoder.melbcode.models.enums.OperateTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class UserFootServiceImpl implements UserFootService {

    @Autowired
    private UserFootDao userFootDao;

    @Override
    public UserFoot saveOrUpdateUserFoot(DocumentTypeEnum documentType, Long documentId, String authorId, String userId,
                                         OperateTypeEnum operateType) {
        // 查询是否有足迹，有的话就更新，没有则新增
        UserFoot userFoot = userFootDao.getUserFootByDocumentAndUserId(userId, documentId, documentType.getCode());
        if (userFoot == null) {
            userFoot = new UserFoot();
            userFoot.setUserId(userId);
            userFoot.setDocumentId(documentId);
            userFoot.setDocumentType(documentType.getCode());
            userFoot.setDocumentUserId(authorId);
            setUserFootStat(userFoot, operateType);
            userFootDao.createUserFoot(userId, documentId, documentType.getCode(), authorId);
        }
        else if (setUserFootStat(userFoot, operateType)) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            userFootDao.updateUserFootById(userFoot, timestamp);
        }

        return userFoot;
    }

    @Override
    public void saveCommentFoot(Comment comment, String articleAuthor, String parentCommentAuthor) {

        // 保存文章对应的评论足迹
        saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, comment.getArticle_id(), articleAuthor, comment.getUser_id(), OperateTypeEnum.COMMENT);

        // 如果评论是子评论，则需要找到父评论的记录，然后设置为已读
        if (comment.getParent_comment_id() != null && comment.getParent_comment_id() != 0) {
            saveOrUpdateUserFoot(DocumentTypeEnum.COMMENT, comment.getParent_comment_id(), parentCommentAuthor, comment.getUser_id(), OperateTypeEnum.COMMENT);
        }

    }

    @Override
    public void removeCommentFoot(Comment comment, String articleAuthor, String parentCommentAuthor) {

    }

    @Override
    public UserFoot queryUserFoot(Long documentId, Integer type, String userId) {
        return userFootDao.getUserFootByDocumentAndUserId(userId, documentId, type);
    }

    private boolean setUserFootStat(UserFoot userFoot, OperateTypeEnum operateType) {
        switch (operateType) {
            case READ:
                // 设置文章为已读
                userFoot.setReadStat(1);
                return true;
            case PRAISE:
            case CANCEL_PRAISE:
                return compareAndUpdate(userFoot::getPraiseStat, userFoot::setPraiseStat, operateType.getDbStatCode());
            case COMMENT:
            case DELETE_COMMENT:
                return compareAndUpdate(userFoot::getCommentStat, userFoot::setCommentStat, operateType.getDbStatCode());
            default:
                return false;
        }
    }

    private <T> boolean compareAndUpdate(Supplier<T> supplier, Consumer<T> consumer, T input) {
        if (Objects.equals(supplier.get(), input)) {
            return false;
        }
        consumer.accept(input);
        return true;
    }
}
