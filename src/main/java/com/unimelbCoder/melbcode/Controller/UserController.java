package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.Service.User.Impl.UserServiceImpl;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class UserController {
    @Autowired
    UserDao userDao;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    UserRelationController userRelationController;

    @GetMapping("/user/{userId}")
    public String getUserInfo(@PathVariable String userId, @RequestHeader(name = "Authorization", required = false) String token){
        // 如果token已经过期了，就重新login
//        if (jwtUtils.isTokenExpired(token)) {
//            return "login";
//        }
        System.out.println(userId);
        User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);
        String isSelf = "0";
        if(userId.equals(userInfo.getId())){
            isSelf = "1";
        }

        //获取基本用户数据
        SimpleUserInfoDTO userBasicInfo = userService.queryUserInfo(userId);

        HashMap<String, String> resInfo = new HashMap<>();

        //查询所请求用户的关注和粉丝
        String follows = Long.toString(userRelationController.userFollowsCount(userId));
        String fans = Long.toString(userRelationController.userFansCount(userId));

        //给结果的data插入数据
        HashMap<String, Object> res = new HashMap<>();
        resInfo.put("username", userBasicInfo.getUsername());
        resInfo.put("role", userBasicInfo.getRole());
        resInfo.put("age", Integer.toString(userBasicInfo.getAge()));
        resInfo.put("intro", userBasicInfo.getIntroduction());
        resInfo.put("following", follows);
        resInfo.put("follower", fans);
        String flag = "ok";

        //将获取到的数据插入响应体中
        res.put("data", resInfo);
        res.put("flag", flag);
        res.put("isSelf", isSelf);
        return JSON.toJSONString(res);
    }
}
