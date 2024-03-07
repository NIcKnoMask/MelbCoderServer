package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.BuiltinExchangeType;
import com.unimelbCoder.melbcode.Service.Rabbitmq.RabbitmqService;
import com.unimelbCoder.melbcode.Service.UserFoot.UserFootService;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.NotifyMsg;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.bean.UserFoot;
import com.unimelbCoder.melbcode.models.dao.ArticleDao;
import com.unimelbCoder.melbcode.models.dao.NotifyMsgDao;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.enums.DocumentTypeEnum;
import com.unimelbCoder.melbcode.models.enums.NotifyTypeEnum;
import com.unimelbCoder.melbcode.models.enums.OperateTypeEnum;
import com.unimelbCoder.melbcode.utils.CommonConstants;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import com.unimelbCoder.melbcode.utils.NotifyMsgEvent;
import com.unimelbCoder.melbcode.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
    UserFootService userFootService;

    @Autowired
    RabbitmqService rabbitmqService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/likeArticle/{articleId}")
    public String likeArticle(@PathVariable Integer articleId,
                              @RequestHeader (name = "Authorization", required = false) String token,
                              @RequestHeader(name = "RequestType", required = false) Integer type)
            throws IOException, TimeoutException {

        HashMap<String, Object> res = new HashMap<>();
        String flag = "error";

        if (token == null && jwtUtils.isTokenExpired(token)) {
            flag = "login";
            res.put("flag", flag);
            return JSON.toJSONString(res);
        }

        OperateTypeEnum operateTypeEnum = OperateTypeEnum.fromCode(type);

        Article art = articleDao.getArticleById(articleId);

        HashMap<String, Object> rabbitMsg = new HashMap<>();

        if (art != null) {
            User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);

            UserFoot userFoot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE,
                    (long)art.getId(), art.getUser_id(),
                    userInfo.getId(), operateTypeEnum);

            // 点赞、收藏消息类型
            NotifyTypeEnum notifyType = OperateTypeEnum.getNotifyType(operateTypeEnum);

            if (notifyType.equals(NotifyTypeEnum.PRAISE) && rabbitmqService.enabled()) {
                rabbitMsg.put("related_id", 0);
                rabbitMsg.put("notify_user_id", art.getUser_id());
                rabbitMsg.put("operate_user_id", userInfo.getId());
                rabbitMsg.put("msg", "praise operation");
                rabbitMsg.put("type", 3);

                rabbitmqService.publishMsg(
                        CommonConstants.EXCHANGE_NAME_DIRECT,
                        BuiltinExchangeType.DIRECT,
                        CommonConstants.QUEUE_KEY_PRAISE,
                        JSON.toJSONString(rabbitMsg));
            }
            else {
                Optional.ofNullable(notifyType).ifPresent(notify -> SpringUtils.publishEvent(new NotifyMsgEvent<>(this, notify, userFoot)));
            }

            flag = "ok";
        }

        res.put("flag", flag);

        return JSON.toJSONString(res);
    }

    @RequestMapping("/allNotifyMsg")
    public Integer queryNotifyMsg(@RequestBody String token) {
        if (token == null || jwtUtils.isTokenExpired(token)) {
            return -1;
        }

        User user = jwtUtils.getUserInfoFromToken(token, User.class);

        List<NotifyMsg> notifyMsg = notifyMsgDao.getNotifyMsgByIdx(user.getId(), 3, 0);

        return notifyMsg.size();
    }

}
