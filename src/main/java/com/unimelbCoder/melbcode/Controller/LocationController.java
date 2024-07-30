package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.bean.Location;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dao.LocationsDao;
import com.unimelbCoder.melbcode.models.dao.UserStarDao;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.text.SimpleDateFormat;


@RestController
public class LocationController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserStarDao userStarDao;

    @Autowired
    LocationsDao locationsDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     *
     * @param map 包含地点的id,
     * @param token 当前用户登录获得的token
     * @return "ok"
     */
    @RequestMapping("/locations/addVisit")
    public String addToVisited(@RequestBody Map<String, Object> map,
                               @RequestHeader(name = "Authorization", required = false) String token){
        HashMap<String, Object> res = new HashMap<>();

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

        return JSON.toJSONString(res);
    }

    /**
     * 将当前的location加入到用户的指定日期的计划中
     * @param map 包含plan的日期, 地点的id, 创建的时间
     * @param token 当前用户登录获得的token
     * @return "ok"
     */
    @RequestMapping("/locations/addStar")
    public String addToStar(@RequestBody Map<String, Object> map,
                               @RequestHeader(name = "Authorization", required = false) String token){
        HashMap<String, Object> res = new HashMap<>();
        res.put("flag", "error");
        // 如果token是空的，或者token已经过期了，就重新login
        if (token == null && jwtUtils.isTokenExpired(token)) {
            res.put("msg", "login");
            return JSON.toJSONString(res);
        }
        User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);

        //再次检查Redis缓存用户信息
        if (!(RedisClient.isUserLogin(userInfo.getUsername()))) {
            res.put("msg", "login");
            return JSON.toJSONString(res);
        }

        try{
            int object_id = Integer.parseInt(map.get("object_id").toString());
            userStarDao.addStar(userInfo.getId(), object_id, map.get("date").toString(), map.get("type").toString());
            res.put("flag", "ok");
            res.put("msg", "successfully add star");
        }catch (Exception e){
            e.printStackTrace();
            res.put("msg", e);
        }

        return JSON.toJSONString(res);
    }

    /**
     * 通过用户计划的plan的日期中的所有地点 规划路径展示在地图上
     * @param map 包含1.plan的日期 2.plan的所有地点的id
     * @param token 当前用户登录获得的token
     * @return 所有所需地理位置的信息, 以及路径规划的结果
     */
    public String getMapPlan(@RequestBody Map<String, Object> map,
                             @RequestHeader(name = "Authorization", required = false) String token){
        HashMap<String, Object> res = new HashMap<>();
        // 通过id从数据库索取所有的经纬度
        // 通过获取到的经纬度进行路径规划
        // 返回结果
        return JSON.toJSONString(res);
    }

    /**
     * 展示location的详情页面
     * @param locationId location的唯一标识
     * @return location的详细数据
     */
    @GetMapping("/locations/getLocInfo/{locationId}")
    public String getLocationInfo(@PathVariable String locationId){
        HashMap<String, Object> res = new HashMap<>();
        res.put("flag", "error");
        Location locInfo = locationsDao.getLocationById(locationId);
        if(locInfo == null){
            return JSON.toJSONString(res);
        }
        res.put("flag", "ok");
        res.put("data", locInfo);
        return JSON.toJSONString(res);
    }
}
