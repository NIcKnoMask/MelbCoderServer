package com.unimelbCoder.melbcode.Controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.Service.Comment.CommentService;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.ArticleDetail;
import com.unimelbCoder.melbcode.bean.Comment;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.ArticleDetailDao;
import com.unimelbCoder.melbcode.models.dao.CommentDao;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.comment.TopCommentDTO;
import com.unimelbCoder.melbcode.models.enums.NotifyTypeEnum;
import com.unimelbCoder.melbcode.utils.CommonConstants;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import com.unimelbCoder.melbcode.utils.NotifyMsgEvent;
import com.unimelbCoder.melbcode.utils.SpringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    UserDao userDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Autowired
    private CommentService commentService;

    @RequestMapping("/createArticle")
    public String createArticle(@RequestBody Map<String, Object> map,
                                @RequestHeader(name = "Authorization", required = false) String token) {

        //检查浏览器缓存不为空
        // 如果token是空的，或者token已经过期了，就重新login
        if (token == null && jwtUtils.isTokenExpired(token)) {
            return "login";
        }

        User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);
        System.out.println(userInfo.toString());

        //再次检查Redis缓存用户信息
        if (!(RedisClient.isUserLogin(userInfo.getUsername()))) {
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

        // Redis逻辑需要抽象
        String key = "unimelb:article:create:" + userInfo.getId();
        Map<Object, Object> articleCache = redisTemplate.opsForHash().entries(key);
        if (!(articleCache.isEmpty())) {
            redisTemplate.delete(key);
        }

        //活跃度事件发布
        processAfterCreateArticle(userInfo.getUsername());

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
        if (!(RedisClient.isUserLogin(userInfo.getUsername()))) {
            return "login";
        }

        map.put("username", userInfo.getId());

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
        HashMap<String, Object> articleData = new HashMap<>();
        HashMap<String, Object> userData = new HashMap<>();
        int count = 1;

        /**
         * 这段代码后需要修改，按照热度等排序，暂时硬编码
         */
        for (int i = 11; i < 14; i++) {
            Article article = articleDao.getArticleById(i);
            User user = userDao.getUserByName(article.getUser_id());
            System.out.println("user: " + user);
            articleData.put("article" + count, article);
            userData.put("user" + count, user);
            count++;
        }

        res.put("articleData", articleData);
        res.put("userData", userData);

        return JSON.toJSONString(res);
    }

    @GetMapping("/article/{id}")
    public String queryArticle(@PathVariable Long id) {
        HashMap<String, Object> res = new HashMap<>();

        Article article = articleDao.getArticleById(id.intValue());
        ArticleDetail articleDetail = articleDetailDao.getArticleDetailByIdx(id.intValue(), 0);
        List<TopCommentDTO> topComments = commentService.getArticleComments(id);

        User user = userDao.getUserByName(article.getUser_id());

        String flag = "error";
        if (article != null && user != null) {
            res.put("article", article);
            res.put("articleDetail", articleDetail);
            res.put("user", user);
            res.put("topComment", topComments);
            flag = "ok";
        }

        res.put("flag", flag);

        return JSON.toJSONString(res);
    }


    private void processAfterCreateArticle(String username) {
        SpringUtils.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.CREATE_ARTICLE, username));
    }

    private Article checkArticleLockByRedis(Integer articleId) {
        Article article = null;

        String redisLockKey = CommonConstants.UNIMELB_CODER + CommonConstants.ARTICLE + CommonConstants.LOCK + articleId;

        //通过键值上锁
        String value = RandomUtil.randomString(6);
        Boolean isLockSuccessful = redisTemplate.opsForValue().setIfAbsent(redisLockKey, value);

        try {
            if (isLockSuccessful) {
                article = articleDao.getArticleById(articleId);
            }
            else {
                Thread.sleep(200);
//                this.queryArticleDetail(articleId);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //此处可抽象比较并删除逻辑
            String redisLockValue = redisTemplate.opsForValue().get(redisLockKey);
            if (!ObjectUtil.isEmpty(redisLockValue) && value.equals(redisLockValue)) {
                redisTemplate.delete(redisLockKey);
            }
        }

        return article;
    }

    private Article checkArticleLockByRedisson(Integer articleId) {
        Article article = null;

        String redisLockKey = CommonConstants.UNIMELB_CODER + CommonConstants.ARTICLE + CommonConstants.LOCK + articleId;
        RLock lock = redissonClient.getLock(redisLockKey);

        try {
            //尝试上锁，上锁30秒后自动解锁，最大等待时间3秒
            if (lock.tryLock(3, 30, TimeUnit.SECONDS)) {
                article = articleDao.getArticleById(articleId);
            }
            else {
                //未获得锁情况，等待后自旋
                Thread.sleep(200);
//                this.queryArticleDetail(articleId);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //操作完成后释放锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return article;
    }

}
