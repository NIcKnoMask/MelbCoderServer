package com.unimelbCoder.melbcode.Service.Comment.converter;

import com.unimelbCoder.melbcode.Service.Comment.model.CommentSaveReq;
import com.unimelbCoder.melbcode.bean.Comment;
import com.unimelbCoder.melbcode.models.dto.comment.BaseCommentDTO;
import com.unimelbCoder.melbcode.models.dto.comment.SubCommentDTO;
import com.unimelbCoder.melbcode.models.dto.comment.TopCommentDTO;

import java.util.ArrayList;

public class CommentConverter {

    public static Comment toBean(CommentSaveReq saveReq) {
        if (saveReq == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setId(saveReq.getCommentId());
        comment.setArticle_id(saveReq.getArticleId());
        comment.setUser_id(saveReq.getUserId());
        comment.setContent(saveReq.getCommentContent());
        comment.setTop_comment_id(saveReq.getTopCommentId() == null? 0L : saveReq.getTopCommentId());
        comment.setParent_comment_id(saveReq.getParentCommentId() == null? 0L : saveReq.getParentCommentId());

        return comment;
    }

    private static <T extends BaseCommentDTO> void parseDto(Comment comment, T sub) {
        sub.setCommentId(comment.getId());
        sub.setUserId(comment.getUser_id());
        sub.setCommentContent(comment.getContent());
        sub.setCommentTime(comment.getCreate_time().getTime());
        sub.setPraiseCount(0);
    }

    public static TopCommentDTO toTopDto(Comment comment) {
        TopCommentDTO dto = new TopCommentDTO();
        parseDto(comment, dto);
        dto.setChildComments(new ArrayList<>());
        return dto;
    }

    public static SubCommentDTO toSubDto(Comment comment) {
        SubCommentDTO dto = new SubCommentDTO();
        parseDto(comment, dto);
        return dto;
    }

}
