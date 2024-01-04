package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.CommentDao;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class CommentController {

    @Autowired
    CommentDao commentDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ArticleDao articleDao;

    @RequestMapping("/topCommentArticle")
    public String topComment(@RequestParam(value = "articleId") Integer articleId,
                             @RequestParam(value = "userId") String userId,
                             @RequestParam(value = "content") String content) {
        Article article = articleDao.getArticleById(articleId);
        User user = userDao.getUserByName(userId);

        String flag = "error";
        HashMap<String, Object> res = new HashMap<>();

        commentDao.createComment(article.getId(), user.getId(), content, 0, 0);

        flag = "ok";
        res.put("flag", flag);

        return JSON.toJSONString(res);
    }

}
