package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.Service.Comment.CommentService;
import com.unimelbCoder.melbcode.Service.Comment.model.CommentSaveReq;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.CommentDao;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import com.unimelbCoder.melbcode.utils.ReqInfoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class CommentController {

    @Autowired
    CommentDao commentDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CommentService commentService;

    @RequestMapping("/topCommentArticle")
    public String topComment(@RequestParam(value = "articleId") Integer articleId,
                             @RequestParam(value = "userId") String userId,
                             @RequestParam(value = "content") String content) {
        Article article = articleDao.getArticleById(articleId);
        User user = userDao.getUserByName(userId);

        String flag = "error";
        HashMap<String, Object> res = new HashMap<>();

        commentDao.createComment((long)article.getId(), user.getId(), content, 0L, 0L);

        flag = "ok";
        res.put("flag", flag);

        return JSON.toJSONString(res);
    }

    @RequestMapping("/createComment")
    public String saveComment(@RequestBody CommentSaveReq saveReq,
                              @RequestHeader(name = "Authorization", required = false) String token) {

        HashMap<String, Object> res = new HashMap<>();;

        // 如果token是空的，或者token已经过期了，就重新login
        if (token == null && jwtUtils.isTokenExpired(token)) {
            res.put("flag", "login");
            return JSON.toJSONString(res);
        }

        User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);

        //再次检查Redis缓存用户信息
        if (!(RedisClient.isUserLogin(userInfo.getUsername()))) {
            res.put("flag", "login");
            return JSON.toJSONString(res);
        }

        // 通过令牌获取用户id
        saveReq.setUserId(userInfo.getId());

        // 保存评论
        commentService.saveComment(saveReq);

        res.put("flag", "ok");

        return JSON.toJSONString(res);
    }

}
