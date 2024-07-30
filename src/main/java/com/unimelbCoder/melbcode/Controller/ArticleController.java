package com.unimelbCoder.melbcode.Controller;

import cn.hutool.core.annotation.Link;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.Service.Article.ArticleService;
import com.unimelbCoder.melbcode.Service.Article.Impl.FanoutService;
import com.unimelbCoder.melbcode.Service.Comment.CommentService;
import com.unimelbCoder.melbcode.Service.User.Impl.UserServiceImpl;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.ArticleDetail;
import com.unimelbCoder.melbcode.bean.Comment;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.ArticleDetailDao;
import com.unimelbCoder.melbcode.models.dao.CommentDao;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;
import com.unimelbCoder.melbcode.models.dto.article.ArticleDTO;
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
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;
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

    @Autowired
    ArticleService articleService;

    @Autowired
    FanoutService fanoutService;

    @Autowired
    UserServiceImpl userService;

    private static final ObjectMapper objectMapper = new ObjectMapper();


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
        processAfterCreateArticle(userInfo.getId());

        //触发fanout 更新其粉丝的timeline
        fanoutService.articleToTimeline(curArticle.getId(), userInfo.getId());

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

    @GetMapping("/article/{id}")
    public String queryArticle(@PathVariable Long id,
                               @RequestHeader(name = "Authorization", required = false) String token) {

        String currentUserId;
        System.out.println(jwtUtils.isTokenExpired(token));
        // 如果token是空的，或者token已经过期了，就重新login
        if (token == null) {
            currentUserId = null;
        }
        else {
            User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);
            currentUserId = userInfo.getId();
            System.out.println(currentUserId);
            // 更新用户历史记录
            userService.userReadHistory(currentUserId, id.intValue());
        }
        ArticleDTO article = new ArticleDTO();
        List<TopCommentDTO> topComments = commentService.getArticleComments(id, currentUserId);

        // 做二级缓存策略
        // 先从缓存寻找文章
        Map<String, Object> cacheArticle = RedisClient.getArticle("hotArticle:" + id);
        if(cacheArticle == null){
            // 从数据库找文章 并存入缓存
            article = articleService.queryFullArticleInfo(id.intValue(), currentUserId);
            cacheArticle = new HashMap<>();
            // 利用反射 从class获取article的类信息
            try{
                Class<?> clazz = article.getClass();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true); // You might need to set accessible to true depending on the visibility of the fields
                    Object value = field.get(article);
                    cacheArticle.put(field.getName(), convertToString(value));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            // 存入redis中
            RedisClient.saveArticle("article_" + id, cacheArticle);
        }else{
            RedisClient.updateExpire("article_" + id);
        }

        HashMap<String, Object> res = new HashMap<>();

        User user = userService.queryUserFullInfo(cacheArticle.get("authorId").toString());

        String flag = "error";
        if (user != null) {
            res.put("article", cacheArticle);
            res.put("articleDetail", cacheArticle.get("content"));
            res.put("username", user.getUsername());
            res.put("userId", user.getId());
            res.put("topComment", topComments);
            res.put("ref_location", cacheArticle.get("ref_loc"));
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

    /**
     * 登录后的用户获取主页按时间线的文章
     * @param id user id
     * @return 40+ articles list as data
     */
    @GetMapping("/home/{id}")
    public String getLatestHomeArticles(@PathVariable String id){
        String key = "timeline_" + id;
        Set<ZSetOperations.TypedTuple<String>> followArticleList = RedisClient.getSortedSetMembers(key, 0, 20);

        String hotKey = "hot_articles";
        Set<ZSetOperations.TypedTuple<String>> hotArticleList = RedisClient.getSortedSetMembers(hotKey,
                0, 40 - followArticleList.size());
        System.out.println(key + ", article list length: " + followArticleList.size());
        // 给Id排序
        Queue<int[]> queue = new PriorityQueue<>((a, b) -> {
            return b[1] - a[1];
        });
        for(ZSetOperations.TypedTuple<String> s: followArticleList){
            int articleId = Integer.parseInt(s.getValue());
            int score = (int)s.getScore().doubleValue();
            queue.offer(new int[]{articleId, score});
        }

        for(ZSetOperations.TypedTuple<String> s: hotArticleList){
            int articleId = Integer.parseInt(s.getValue());
            int score = (int)s.getScore().doubleValue();
            queue.offer(new int[]{articleId, score});
        }

        // 获取文章的标题和简介，返回给前端
        List<Integer> articleIdList = new LinkedList<>();
        while(!queue.isEmpty()){
            articleIdList.add(queue.poll()[0]);
        }

        for(Integer artId: articleIdList){
            System.out.println("article: " + artId);
        }

        List<Article> articleList = new LinkedList<>();
        if(articleIdList.size() != 0){
            articleList = articleDao.getArticlesByIds(articleIdList);
        }

        HashMap<String, Object> res = new HashMap<>();
        res.put("flag", "ok");
        res.put("data", articleList);
        return JSON.toJSONString(res);
    }


    public static String convertToString(Object value){
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return value.toString(); // Directly convert Boolean to "true" or "false"
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toInstant().toString(); // Convert Timestamp to string
        }
        if (value instanceof Integer || value instanceof String) {
            return value.toString(); // Direct conversion for Integer and String
        }
        try {
            return objectMapper.writeValueAsString(value);  // Convert complex objects to JSON string
        } catch (Exception e) {
            throw new RuntimeException("Error during string conversion", e);
        }
    }
}
