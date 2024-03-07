package com.unimelbCoder.melbcode.Service.Comment;

import com.unimelbCoder.melbcode.Service.Comment.model.CommentSaveReq;
import com.unimelbCoder.melbcode.bean.Comment;
import com.unimelbCoder.melbcode.models.dto.comment.TopCommentDTO;

import java.util.List;

public interface CommentService {
    /**
     * 通过文章Id获取到顶层评论，再获得顶层评论中的下层评论
     * @param articleId
     * @return
     */
    List<TopCommentDTO> getArticleComments(Long articleId, String userId);

    Long saveComment(CommentSaveReq saveReq);

    void deleteComment(Long commentId, Long userId);

}
