package com.unimelbCoder.melbcode.Controller;


import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.models.dao.UserRelationDao;
import com.unimelbCoder.melbcode.models.dto.UserRelationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserRelationController {

    @Autowired
    UserRelationDao userRelation;

    @PostMapping("/relation/follow")
    public String followUser(@RequestBody UserRelationDTO req){
        boolean hasData = userRelation.hasRelation(req.getMainUserId(), req.getFollowUserId());

        String flag = "ok";
        HashMap<String, Object> res = new HashMap<>();

        if(hasData){
            // set follow state = 1
            userRelation.updateFollow(req.getMainUserId(), req.getFollowUserId());
        }else{
            // create a row
            userRelation.followUser(req.getMainUserId(), req.getFollowUserId());
        }

        // 需要更新当前关注人数一并返回给前端
        Long followCounts = userFollowsCount(req.getMainUserId());
        res.put("flag", flag);
        res.put("data", followCounts);
        return JSON.toJSONString(res);
    }

    @PostMapping("/relation/unfollow")
    public String unFollowUser(@RequestBody UserRelationDTO req){
        boolean hasData = userRelation.hasRelation(req.getMainUserId(), req.getFollowUserId());

        String flag = "error";
        HashMap<String, Object> res = new HashMap<>();

        if(hasData){
            // set follow state = 2
            userRelation.unFollowUser(req.getMainUserId(), req.getFollowUserId());
            flag = "ok";
        }

        // 需要更新当前关注人数一并返回给前端
        Long followCounts = userFollowsCount(req.getMainUserId());
        res.put("flag", flag);
        res.put("data", followCounts);
        return JSON.toJSONString(res);
    }

    @GetMapping("/relation/{targets}")
    public String hasFollow(@PathVariable String targets){
        String[] tgs = targets.split(":");
        boolean relation = false;
        boolean isSelf = false;
        String flag = "error";
        if(!tgs[1].equals("") && !tgs[0].equals("")){
            if(tgs[1] == tgs[0]){
                isSelf = true;
                relation = true;
            }else{
                relation = userRelation.hasRelation(tgs[0], tgs[1]);
            }
            flag = "ok";
        }
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("relation", relation);
        data.put("isSelf", isSelf);

        res.put("flag", flag);
        res.put("data", data);
        return JSON.toJSONString(res);
    }

    public List<UserRelationDTO> listFollows(String userId){
        return userRelation.listUserFollows(userId);
    }

    public List<UserRelationDTO> listFans(String userId){
        return userRelation.queryUserFans(userId);
    }


    public Long userFollowsCount(String userId){
        List<UserRelationDTO> followList = listFollows(userId);
        return (long) followList.size();
    }

    public Long userFansCount(String userId){
        List<UserRelationDTO> followList = listFans(userId);
        return (long) followList.size();
    }

}
