package com.unimelbCoder.melbcode.Controller;


import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.models.dao.UserRelationDao;
import com.unimelbCoder.melbcode.models.dto.UserRelationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserRelationController {

    @Autowired
    UserRelationDao userRelation;

    @PostMapping("/relation/follow")
    public String followUser(@RequestBody UserRelationDTO req){
        boolean hasData = userRelation.hasUser(req.getMainUsername(), req.getFollowUsername());

        String flag = "ok";
        HashMap<String, Object> res = new HashMap<>();

        if(hasData){
            // set follow state = 1
            userRelation.updateFollow(req.getMainUsername(), req.getFollowUsername());
        }else{
            // create a row
            userRelation.followUser(req.getMainUsername(), req.getFollowUsername());
        }

        // 需要更新当前关注人数一并返回给前端
        Long followCounts = userFollowsCount(req.getMainUsername());
        res.put("flag", flag);
        res.put("data", followCounts);
        return JSON.toJSONString(res);
    }

    @PostMapping("/relation/unfollow")
    public String unFollowUser(@RequestBody UserRelationDTO req){
        boolean hasData = userRelation.hasUser(req.getMainUsername(), req.getFollowUsername());

        String flag = "error";
        HashMap<String, Object> res = new HashMap<>();

        if(hasData){
            // set follow state = 2
            userRelation.unFollowUser(req.getMainUsername(), req.getFollowUsername());
            flag = "ok";
        }

        // 需要更新当前关注人数一并返回给前端
        Long followCounts = userFollowsCount(req.getMainUsername());
        res.put("flag", flag);
        res.put("data", followCounts);
        return JSON.toJSONString(res);
    }

    public List<UserRelationDTO> listFollows(String username){
        return userRelation.listUserFollows(username);
    }


    public Long userFollowsCount(String username){
        List<UserRelationDTO> followList = listFollows(username);
        return (long) followList.size();
    }

    public Long userFansCount(String username){
        List<UserRelationDTO> followList = listFollows(username);
        return (long) followList.size();
    }

}
