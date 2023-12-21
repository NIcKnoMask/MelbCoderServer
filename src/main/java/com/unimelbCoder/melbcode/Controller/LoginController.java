package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.enums.NotifyTypeEnum;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import com.unimelbCoder.melbcode.utils.NotifyMsgEvent;
import com.unimelbCoder.melbcode.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {
    @Autowired
    UserDao userDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    JwtUtils jwtUtils;

    @RequestMapping("/login")
    public String login(@RequestBody User user){
        User us = userDao.getUserByMessage(user.getUsername(), user.getPassword());

        String flag = "error";
        HashMap<String, Object> res = new HashMap<>();
        String token = null;

        if(us != null){
            flag = "ok";
            token = jwtUtils.generateToken(us);
            // set redis
            System.out.println(redisTemplate);
            redisTemplate.opsForValue().set(us.getUsername(), token, 10, TimeUnit.MINUTES);
        }
        res.put("flag", flag);
        res.put("data", token);
        processAfterLogin(us.getUsername());
        return JSON.toJSONString(res);
    }

    private void processAfterLogin(String username){
        SpringUtils.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.LOGIN, username));
    }

}
