package com.unimelbCoder.melbcode.models.dao;

import com.unimelbCoder.melbcode.bean.UserFoot;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserFootDao {

    // TODO: 根据用户表单更新后合并修改

    public UserFoot getUserFootByDocumentAndUserId(@Param("user_id") String userId, @Param("document_id") Long documentId,
                                                   @Param("document_type") Integer documentType);

    public List<UserFoot> countArticleByUserId(@Param("document_user_id") Long userId);

    public Long countCommentPraise(@Param("document_id") Long documentId);

    public void createUserFoot(@Param("user_id") String userId, @Param("document_id") Long documentId,
                               @Param("document_type") Integer documentType, @Param("document_user_id") String authorId);

    public void updateUserFootById(@Param("userFoot") UserFoot userFoot, @Param("update_time") Timestamp updateTime);
}
