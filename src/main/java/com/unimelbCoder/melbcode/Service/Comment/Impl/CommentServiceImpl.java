package com.unimelbCoder.melbcode.Service.Comment.Impl;

import com.unimelbCoder.melbcode.Service.Comment.CommentService;
import com.unimelbCoder.melbcode.Service.Comment.converter.CommentConverter;
import com.unimelbCoder.melbcode.Service.Comment.model.CommentSaveReq;
import com.unimelbCoder.melbcode.Service.User.UserService;
import com.unimelbCoder.melbcode.Service.UserFoot.UserFootService;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.Comment;
import com.unimelbCoder.melbcode.bean.UserFoot;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.CommentDao;
import com.unimelbCoder.melbcode.models.dao.UserFootDao;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;
import com.unimelbCoder.melbcode.models.dto.comment.BaseCommentDTO;
import com.unimelbCoder.melbcode.models.dto.comment.SubCommentDTO;
import com.unimelbCoder.melbcode.models.dto.comment.TopCommentDTO;
import com.unimelbCoder.melbcode.models.enums.NotifyTypeEnum;
import com.unimelbCoder.melbcode.utils.NotifyMsgEvent;
import com.unimelbCoder.melbcode.utils.ReqInfoContext;
import com.unimelbCoder.melbcode.utils.SpringUtils;
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

    @Autowired
    UserFootDao userFootDao;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    private UserFootService userFootService;

    @Override
    public List<TopCommentDTO> getArticleComments(Long articleId) {

        // 1.查询顶级评论
        List<Comment> allComments = commentDao.getTopCommentList(articleId);
        if (allComments.isEmpty()) {
            return Collections.emptyList();
        }

        // 做comment id和DTO的映射
        Map<Long, TopCommentDTO> topComments = allComments.stream().collect(
                Collectors.toMap(Comment::getId, CommentConverter::toTopDto));

        // 2. 查询非一级评论
        List<Comment> subComments = new ArrayList<>();
        for (Long topCommentId: topComments.keySet()) {
            subComments.addAll(commentDao.getSubCommentList(articleId, topCommentId));
        }

        // 3. 构建评论父子关系
        buildCommentRelation(subComments, topComments);

        // 4. 挑选出需要返回的数据，排序，补齐用户信息，返回
        List<TopCommentDTO> ans = new ArrayList<>();
        allComments.forEach(comment -> {
            TopCommentDTO dto = topComments.get(comment.getId());
            fillTopCommentInfo(dto);
            ans.add(dto);
        });

        //根据时间排序
        Collections.sort(ans);
        return ans;

    }

    private void buildCommentRelation(List<Comment> subComments, Map<Long, TopCommentDTO> topComments) {
        Map<Long, SubCommentDTO> subCommentMap = subComments.stream().collect(
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

    private void fillTopCommentInfo(TopCommentDTO commentDTO) {
        fillCommentInfo(commentDTO);
        commentDTO.getChildComments().forEach(this::fillCommentInfo);
        Collections.sort(commentDTO.getChildComments());
    }
    
    private void fillCommentInfo(BaseCommentDTO comment) {
        // TODO: 需要将user部分重构表单结合写
        SimpleUserInfoDTO simpleUserInfoDTO = userService.queryUserInfo(comment.getUserId());
        if (simpleUserInfoDTO == null) {
            // 用户注销情况，给一个默认用户
            comment.setUserName("DefaultUser");
            if (comment instanceof TopCommentDTO) {
                ((TopCommentDTO) comment).setCommentCount(0);
            }
        }
        else {
            comment.setUserName(simpleUserInfoDTO.getUsername());
            if (comment instanceof TopCommentDTO) {
                ((TopCommentDTO) comment).setCommentCount(((TopCommentDTO) comment).getChildComments().size());
            }
        }

        // TODO: 用户足迹问题
//        Long praiseCount = userFootDao.countCommentPraise(comment.getCommentId());
//        comment.setPraiseCount(praiseCount.intValue());

        // 查询当前登录用户是否点赞过
        // TODO: 用户修改后的对齐问题
//        String currentUserId = ReqInfoContext.getReqInfo().getUserId();
//        if (currentUserId != null) {
//            // 判断当前用户是否点赞过
//            UserFoot userFoot = userFootService.queryUserFoot(comment.getCommentId(), 2, currentUserId);
//            comment.setPraised(userFoot != null && Objects.equals(userFoot.getPraiseStat(), 1));
//        }
//        else {
//            comment.setPraised(false);
//        }
    }

    @Override
    public Long saveComment(CommentSaveReq saveReq) {
        // 保存评论
        Comment comment;
        if (saveReq.getCommentId() == null || saveReq.getCommentId() == 0) {
            comment = addComment(saveReq);
        }
        else {
            comment = updateComment(saveReq);
        }

        return comment.getId();
    }

    private Comment addComment(CommentSaveReq saveReq) {
        // 1. 获取父评论信息
        String parentCommentUser = getParentCommentUser(saveReq.getParentCommentId());

        // 2. 保存评论内容
        Comment comment = CommentConverter.toBean(saveReq);
        commentDao.createComment(comment.getArticle_id(), comment.getUser_id(), comment.getContent(),
                comment.getTop_comment_id(), comment.getParent_comment_id());

        // 3. 保存用户足迹信息：文章的已评信息 + 评论的已评信息
        Article article = articleDao.getArticleById(saveReq.getArticleId().intValue());
        if (article == null) {
            throw new NullPointerException("文章不存在：文章=" + saveReq.getArticleId());
        }
        userFootService.saveCommentFoot(comment, article.getUser_id(), parentCommentUser);

        // 4. 发布评论事件，用于活跃度积分
        SpringUtils.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.COMMENT, comment));
        if (parentCommentUser != null) {
            SpringUtils.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.REPLY, comment));
        }

        return comment;
    }

    private Comment updateComment(CommentSaveReq saveReq) {
        // TODO: 实际更新逻辑需要实现
        Comment comment = new Comment();
        return comment;
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {

    }

    private String getParentCommentUser(Long parentCommentId) {
        if (parentCommentId == null || parentCommentId == 0L) {
            return null;
        }

        Comment parent = commentDao.getCommentById(parentCommentId);
        if (parent == null) {
            throw new NullPointerException("父级评论不存在：父评论=" + parentCommentId);
        }
        return parent.getUser_id();

    }
}
