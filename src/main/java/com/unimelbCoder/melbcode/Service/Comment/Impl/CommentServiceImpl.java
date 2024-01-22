package com.unimelbCoder.melbcode.Service.Comment.Impl;

import com.unimelbCoder.melbcode.Service.Comment.CommentService;
import com.unimelbCoder.melbcode.Service.Comment.converter.CommentConverter;
import com.unimelbCoder.melbcode.Service.User.UserService;
import com.unimelbCoder.melbcode.bean.Comment;
import com.unimelbCoder.melbcode.models.dao.CommentDao;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;
import com.unimelbCoder.melbcode.models.dto.comment.BaseCommentDTO;
import com.unimelbCoder.melbcode.models.dto.comment.SubCommentDTO;
import com.unimelbCoder.melbcode.models.dto.comment.TopCommentDTO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    CommentDao commentDao;

    @Autowired
    UserService userService;

    @Override
    public List<TopCommentDTO> getArticleComments(Integer articleId) {

        // 1.查询顶级评论
        List<Comment> allComments = commentDao.getTopCommentList(articleId);
        if (allComments.isEmpty()) {
            return Collections.emptyList();
        }

        // 做comment id和DTO的映射
        Map<Integer, TopCommentDTO> topComments = allComments.stream().collect(
                Collectors.toMap(Comment::getId, CommentConverter::toTopDto));

        // 2. 查询非一级评论
        List<Comment> subComments = new ArrayList<>();
        for (Integer topCommentId: topComments.keySet()) {
            subComments.addAll(commentDao.getSubCommentList(articleId, topCommentId));
        }

        // 3. 构建评论父子关系
        buildCommentRelation(subComments, topComments);

        // 4. 挑选出需要返回的数据，排序，补齐用户信息，返回
        List<TopCommentDTO> ans = new ArrayList<>();
        allComments.forEach(comment -> {
            TopCommentDTO dto =topComments.get(comment.getId());
            ans.add(dto);
        });

        //根据时间排序
        Collections.sort(ans);
        return ans;

    }

    private void buildCommentRelation(List<Comment> subComments, Map<Integer, TopCommentDTO> topComments) {
        Map<Integer, SubCommentDTO> subCommentMap = subComments.stream().collect(
                Collectors.toMap(Comment::getId, CommentConverter::toSubDto));
        subComments.forEach(comment -> {
            TopCommentDTO top = topComments.get(comment.getTop_comment_id());
            if (top == null) {
                return;
            }

            SubCommentDTO sub = subCommentMap.get(comment.getId());
            top.getChildComments().add(sub);
            if (Objects.equals(comment.getParent_comment_id(), comment.getTop_comment_id())) {
                return;
            }

            SubCommentDTO parent = subCommentMap.get(comment.getParent_comment_id());
            sub.setParentContent(parent == null ? "已删除。。。" : parent.getCommentContent());
        });
    }
    
    private void fillCommentInfo(BaseCommentDTO comment) {
        // TODO: 需要将user部分重构表单结合写
        SimpleUserInfoDTO simpleUserInfoDTO = userService.queryUserInfo(comment.getUserName());
        if (simpleUserInfoDTO == null) {}
    }
}
