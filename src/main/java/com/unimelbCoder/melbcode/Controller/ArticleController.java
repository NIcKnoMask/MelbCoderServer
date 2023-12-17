package com.unimelbCoder.melbcode.Controller;

import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.ArticleDetail;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.dao.ArticleDao;
import com.unimelbCoder.melbcode.dao.ArticleDetailDao;
import com.unimelbCoder.melbcode.dao.UserDao;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class ArticleController {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    ArticleDetailDao articleDetailDao;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/createArticle")
    public String createArticle(@RequestBody Map<String, Object> map) {

        //检查浏览器缓存不为空
        LinkedHashMap<String, String> tokenMap = (LinkedHashMap<String, String>) map.get("username");
        String token = tokenMap.get("token");

        if (token == null) {
            return "login";
        }

        User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);

        //再次检查Redis缓存用户信息
        String checkToken = redisTemplate.opsForValue().get(userInfo.getUsername());

        if (checkToken == null) {
            return "login";
        }

        System.out.println("user creating article");

        Article art = articleDao.getArticleByTitle((String) map.get("title"));

        if (art != null) {
            System.out.println("already exist article id");
            return "failed";
        }
        articleDao.createArticle(
                userInfo.getId(),
                Integer.parseInt((String) map.get("article_type")),
                (String) map.get("title"),
                (String) map.get("short_title"),
                Integer.parseInt((String) map.get("category")));

        System.out.println("successfully insert the article row");

        Article curArticle = articleDao.getArticleByTitle((String) map.get("title"));
        ArticleDetail artDetail = articleDetailDao.getArticleDetailByIdx(curArticle.getId(), 0);

        if (artDetail != null) {
            System.out.println("already exist article detail index");
            return "failed";
        }

        articleDetailDao.createArticleDetail(curArticle.getId(), 0, (String) map.get("content"));
        System.out.println("successfully insert the article detail row");

        return "ok";
    }

}
