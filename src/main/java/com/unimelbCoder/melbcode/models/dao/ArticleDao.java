package com.unimelbCoder.melbcode.models.dao;

import com.unimelbCoder.melbcode.bean.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ArticleDao {
    public Article getArticleById(@Param("id") Integer id);

    public Article getArticleByCategory(@Param("category_id") String category_id);

    public Article getArticleByTitle(@Param("title") String title);

    public Article getArticleBySTitle(@Param("short_title") String short_title);

    public void createArticle(@Param("user_id") String user_id, @Param("article_type") int article_type,
                              @Param("title") String title, @Param("short_title") String short_title,
                              @Param("category_id") int category_id);

    public List<Article> getArticlesByIds(@Param("idsList") List<Integer> ids);
}
