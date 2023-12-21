package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.ArticleDetail;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.ArticleDetailDao;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
    public String createArticle(@RequestBody Map<String, Object> map,
                                @RequestHeader(name = "Authorization", required = false) String token) {

        //检查浏览器缓存不为空
//        LinkedHashMap<String, String> tokenMap = (LinkedHashMap<String, String>) map.get("username");


        // 如果token是空的，或者token已经过期了，就重新login
        if (token == null && jwtUtils.isTokenExpired(token)) {
            return "login";
        }

        User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);
        System.out.println(userInfo.toString());

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

        //Redis逻辑需要抽象
        String key = "unimelb:article:create:" + userInfo.getId();
        Map<Object, Object> articleCache = redisTemplate.opsForHash().entries(key);
        if (!(articleCache.isEmpty())) {
            redisTemplate.opsForHash().delete(key, articleCache.keySet());
        }

        return "ok";
    }

    @RequestMapping("/editCreateArticle")
    public String editArticle(@RequestBody Map<String, Object> map,
                              @RequestHeader (name = "Authorization", required = false) String token,
                              @RequestHeader (name = "RequestType", required = false) String type) {
        // 如果token是空的，或者token已经过期了，就重新login
        if (token == null && jwtUtils.isTokenExpired(token)) {
            return "login";
        }

        User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);

        //再次检查Redis缓存用户信息
        String checkToken = redisTemplate.opsForValue().get(userInfo.getUsername());

        if (checkToken == null) {
            return "login";
        }

        map.put("username", Integer.toString(userInfo.getId()));

        Article article = articleDao.getArticleByTitle((String) map.get("title"));

        String flag = "error";
        HashMap<String, Object> res = new HashMap<>();

        if (article == null) {
            flag = "ok";

            //set redis
            String key = "unimelb:article:create:" + userInfo.getId();
            System.out.println("map = " + map);
            if (Objects.equals(type, "save")) {
                System.out.println("save called");
                redisTemplate.opsForHash().putAll(key, map);
                redisTemplate.expire(key, 30, TimeUnit.MINUTES);
            }
            else {
                System.out.println("load called");
                Map<Object, Object> articleCache = redisTemplate.opsForHash().entries(key);
                if (articleCache.isEmpty()) {
                    redisTemplate.opsForHash().putAll(key, map);
                    redisTemplate.expire(key, 30, TimeUnit.MINUTES);
                    res.put("data", map);
                }
                else {
                    res.put("data", articleCache);
                }
            }

        }

        res.put("flag", flag);

        return JSON.toJSONString(res);
    }

    @RequestMapping("/allArticle")
    public String queryAllArticle() {
        HashMap<String, Object> res = new HashMap<>();

        for (int i = 4; i < 7; i++) {
            Article article = articleDao.getArticleById(i);
            res.put(Integer.toString(i), article);
        }

        return JSON.toJSONString(res);
    }

}
