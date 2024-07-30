package com.unimelbCoder.melbcode.Service.User;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.models.dao.UserStarDao;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.Location;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.SimpleDateFormat;

@Service
public class UserStarService {
    @Resource
    UserStarDao userStarDao;

    /**
     * 获取用户在收藏夹中的收藏
     * @param userId 用户的id
     * @return type=1 用户收藏的所有文章
     */
    public List<Article> getStarArticle(String userId){
        return userStarDao.getStarArticle(userId);
    }

    /**
     * 获取用户在收藏夹中的收藏
     * @param userId 用户的id
     * @return type=2 用户收藏的所有location. date作为key, 对应的收藏的list
     */
    public Map<String, List<Map<String, Object>>> getStarLocation(String userId){
        List<Map<String, Object>> starLocations = userStarDao.getStarLocations(userId);
        Map<String, List<Map<String, Object>>> res = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Map<String, Object> loc : starLocations){
            java.sql.Date date = (java.sql.Date) loc.get("star_date");
            String dateString = sdf.format(date);
            res.computeIfAbsent(dateString, k -> new ArrayList<>()).add(loc);
        }
        return res;
    }

    public List<Location> getStarLocationByDate(String userId, String date){
        return userStarDao.getStarLocationByDate(userId, date);
    }
}
