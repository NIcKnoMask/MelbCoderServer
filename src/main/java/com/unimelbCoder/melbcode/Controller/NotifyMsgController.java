package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.BuiltinExchangeType;
import com.unimelbCoder.melbcode.Service.Rabbitmq.RabbitmqService;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.NotifyMsg;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.NotifyMsgDao;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.enums.NotifyTypeEnum;
import com.unimelbCoder.melbcode.utils.CommonConstants;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import com.unimelbCoder.melbcode.utils.ReqInfoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class NotifyMsgController {

    @Autowired
    NotifyMsgDao notifyMsgDao;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    UserDao userDao;

    @Autowired
    RabbitmqService rabbitmqService;

    @Autowired
    JwtUtils jwtUtils;

    @RequestMapping("/likeArticle")
    public String likeArticle(@RequestBody Article article,
                              @RequestHeader (name = "Authorization", required = false) String token,
                              @RequestHeader(name = "RequestType", required = false) Integer type)
            throws IOException, TimeoutException {

        if (token == null && jwtUtils.isTokenExpired(token)) {
            return "login";
        }

        Article art = articleDao.getArticleByTitle(article.getTitle());

        String flag = "error";
        HashMap<String, Object> res = new HashMap<>();
        HashMap<String, Object> rabbitMsg = new HashMap<>();

        if (art != null && type.equals(NotifyTypeEnum.PRAISE.getType()) && rabbitmqService.enabled()) {
            User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);

            rabbitMsg.put("related_id", 0);
            rabbitMsg.put("notify_user_id", article.getUser_id());
            rabbitMsg.put("operate_user_id", userInfo.getId());
            rabbitMsg.put("msg", "praise operation");
            rabbitMsg.put("type", 3);

            rabbitmqService.publishMsg(
                    CommonConstants.EXCHANGE_NAME_DIRECT,
                    BuiltinExchangeType.DIRECT,
                    CommonConstants.QUEUE_KEY_PRAISE,
                    JSON.toJSONString(rabbitMsg));
            flag = "ok";
        }

        res.put("flag", flag);

        return JSON.toJSONString(res);
    }

    @RequestMapping("/allNotifyMsg")
    public Integer queryNotifyMsg(@RequestBody String token) {
        if (token == null && jwtUtils.isTokenExpired(token)) {
            return -1;
        }

        User user = jwtUtils.getUserInfoFromToken(token, User.class);

        List<NotifyMsg> notifyMsg = notifyMsgDao.getNotifyMsgByIdx(user.getId(), 3, 0);

        return notifyMsg.size();
    }

}
