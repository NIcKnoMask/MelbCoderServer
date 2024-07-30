package com.unimelbCoder.melbcode.Service.Article.Impl;

import com.unimelbCoder.melbcode.Service.Article.ArticleConverter;
import com.unimelbCoder.melbcode.Service.Article.ArticleService;
import com.unimelbCoder.melbcode.Service.Statistics.CountService;
import com.unimelbCoder.melbcode.Service.UserFoot.UserFootService;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.ArticleDetail;
import com.unimelbCoder.melbcode.bean.UserFoot;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.ArticleDetailDao;
import com.unimelbCoder.melbcode.models.dto.article.ArticleDTO;
import com.unimelbCoder.melbcode.models.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Component
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    ArticleDetailDao articleDetailDao;

    @Autowired
    CountService countService;

    @Autowired
    UserFootService userFootService;

    @Override
    public ArticleDTO queryDetailArticleInfo(Integer articleId) {
        ArticleDTO article = queryArticleDetail(articleId);
        if (article == null) {
            throw new NullPointerException("文章不存在，文章=" + articleId);
        }

        // TODO：更新Category信息

        // TODO：更新Tag信息

        return article;
    }

    /**
     * 查询文章所有的关联信息，正文，分类，标签，阅读计数，当前登录用户是否点赞、评论过
     *
     * @param articleId
     * @param currentUser
     * @return
     */
    @Override
    public ArticleDTO queryFullArticleInfo(Integer articleId, String currentUser) {
        ArticleDTO article = queryDetailArticleInfo(articleId);

        // TODO: 文章计数加1
        countService.incrArticleReadCount(currentUser, articleId);

        // 根据登录用户，进行操作标记
        if (currentUser != null) {
            // 更新用户足迹，如是否点赞、评论、收藏过
            UserFoot userFoot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, (long)articleId, article.getAuthorId(),
                    currentUser, OperateTypeEnum.READ);
            article.setPraised(Objects.equals(userFoot.getPraiseStat(), PraiseStatEnum.PRAISE.getCode()));
            article.setCommented(Objects.equals(userFoot.getCommentStat(), CommentStatEnum.COMMENT.getCode()));
            article.setCollected(Objects.equals(userFoot.getCollectionStat(), CollectionStatEnum.COLLECT.getCode()));
        }
        else {
            // 若未登录，全部按默认处理
            article.setPraised(false);
            article.setCommented(false);
            article.setCollected(false);
        }

        // 更新文章计数统计
        article.setCount(countService.queryArticleStatisticInfo(articleId));

        // TODO: 设置文章的点赞列表

        return article;
    }

    public ArticleDTO queryArticleDetail(Integer articleId) {
        Article article = articleDao.getArticleById(articleId);
        if (article == null || Objects.equals(article.getDeleted(), 1)) {
            return null;
        }

        // 查询文章正文，设置DTO
        ArticleDTO dto = ArticleConverter.toDto(article);
        ArticleDetail detail = articleDetailDao.getArticleDetailByIdx(articleId, 0);
        dto.setContent(detail.getContent());
        dto.setRef_loc(detail.getRef_loc());

        return dto;
    }
}
