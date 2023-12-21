package com.unimelbCoder.melbcode.models.dao;

import com.unimelbCoder.melbcode.bean.ArticleDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDetailDao {

    public ArticleDetail getArticleDetailById(@Param("id") int id);

    public ArticleDetail getArticleDetailByIdx(@Param("article_id") int article_id, @Param("version") int version);

    public void createArticleDetail(@Param("article_id") int article_id, @Param("version") int version,
                                    @Param("content") String content);
}
