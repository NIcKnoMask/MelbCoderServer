package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.NotifyMsgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
public class NotifyMsgController {

    @Autowired
    NotifyMsgDao notifyMsgDao;

    @Autowired
    ArticleDao articleDao;

    @RequestMapping("/like")
    public String likeArticle(@RequestBody Article article) {

        Article art = articleDao.getArticleByTitle(article.getTitle());

        String flag = "error";
        HashMap<String, Object> res = new HashMap<>();
        String token = null;

        if (art != null){
            flag = "ok";
        }

        return JSON.toJSONString(res);
    }

}
