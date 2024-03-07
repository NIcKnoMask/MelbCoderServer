package com.unimelbCoder.melbcode.Service.Article;

import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.models.dto.article.ArticleDTO;

/**
 * 文章Bean转换DTO用
 */
public class ArticleConverter {
        // TODO: DTO转Bean需要实现
//    public static Article toArticleBean(ArticlePostReq req, String author) {
//        Article article = new Article();
//        // 设置作者ID
//        article.setUser_id(author);
//        article.setId(req.getArticleId());
//        article.setTitle(req.getTitle());
//        article.setShortTitle(req.getShortTitle());
//        article.setArticleType(ArticleTypeEnum.valueOf(req.getArticleType().toUpperCase()).getCode());
//        article.setPicture(req.getCover() == null ? "" : req.getCover());
//        article.setCategoryId(req.getCategoryId());
//        article.setSource(req.getSource());
//        article.setSourceUrl(req.getSourceUrl());
//        article.setSummary(req.getSummary());
//        article.setStatus(req.pushStatus().getCode());
//        article.setDeleted(req.deleted() ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
//        return article;
//    }

    public static ArticleDTO toDto(Article article) {
        if (article == null) {
            return null;
        }
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setAuthorId(article.getUser_id());
        articleDTO.setArticleId(article.getId());
        articleDTO.setArticleType(article.getArticle_type());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setShort_title(article.getShort_title());
//        articleDTO.setSummary(articleDO.getSummary());
//        articleDTO.setCover(articleDO.getPicture());
//        articleDTO.setSourceType(SourceTypeEnum.formCode(articleDO.getSource()).getDesc());
//        articleDTO.setSourceUrl(articleDO.getSourceUrl());
        articleDTO.setPublicStatus(article.getPublic_status());
        articleDTO.setCreate_time(article.getCreate_time());
        articleDTO.setUpdate_time(article.getUpdate_time());
//        articleDTO.setOfficalStat(article.getOfficalStat());
//        articleDTO.setToppingStat(article.getToppingStat());
//        articleDTO.setCreamStat(article.getCreamStat());

        // 设置类目id
//        articleDTO.setCategory(new CategoryDTO(articleDO.getCategoryId(), null));
        return articleDTO;
    }

}
