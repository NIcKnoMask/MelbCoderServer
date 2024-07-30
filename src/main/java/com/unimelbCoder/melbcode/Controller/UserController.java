package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.Service.Rank.Impl.UserRankServiceImpl;
import com.unimelbCoder.melbcode.Service.User.Impl.UserServiceImpl;
import com.unimelbCoder.melbcode.Service.User.UserStarService;
import com.unimelbCoder.melbcode.bean.Article;
import com.unimelbCoder.melbcode.bean.Location;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.MessageItemDTO;
import com.unimelbCoder.melbcode.models.dto.RankItemDTO;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;
import com.unimelbCoder.melbcode.models.enums.ActivityRankTimeEnum;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.text.SimpleDateFormat;

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

    @Autowired
    UserRankServiceImpl userRankService;

    @Autowired
    UserStarService userStarService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/user/{userId}")
    public String getUserInfo(@PathVariable String userId){
        System.out.println(userId);
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
        return JSON.toJSONString(res);
    }

    @GetMapping("/user/activity")
    public String getUserActivity(){
        Map<String, Object> res = new HashMap<>();
        List<RankItemDTO> rankList = userRankService.queryRankList(ActivityRankTimeEnum.DAY, 10);
        if(rankList != null){
            res.put("flag", "ok");
            res.put("data", rankList);
        }else{
            res.put("flag", "error");
            res.put("data", null);
        }
        return JSON.toJSONString(res);
    }

    @GetMapping("/user/star/article/{userId}")
    public String getStarArticles(@PathVariable String userId){
        HashMap<String, Object> res = new HashMap<>();
        res.put("flag", false);
        try{
            List<Article> starInfo = userStarService.getStarArticle(userId);
            res.put("flag", true);
            res.put("data", starInfo);
        }catch (Exception e){
            e.printStackTrace();
            res.put("msg", e);
            return JSON.toJSONString(res);
        }
        return JSON.toJSONString(res);
    }

    @RequestMapping("/user/star/dateStarLocation")
    public String getStarLocations(@RequestBody Map<String, String> map,
                                   @RequestHeader(name = "Authorization", required = false) String token){
        System.out.println(map.get("plan_date"));
        //检查浏览器缓存不为空
        // 如果token是空的，或者token已经过期了，就重新login
        if (token == null && jwtUtils.isTokenExpired(token)) {
            return "login";
        }

        User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);

        //再次检查Redis缓存用户信息
        if (!(RedisClient.isUserLogin(userInfo.getUsername()))) {
            return "login";
        }

        HashMap<String, Object> res = new HashMap<>();
        res.put("flag", "error");

        try{
            List<Location> starInfo = userStarService.getStarLocationByDate(userInfo.getId(),
                    map.get("plan_date"));
            res.put("flag", "ok");
            res.put("data", starInfo);
        }catch (Exception e){
            e.printStackTrace();
            res.put("msg", e);
            return JSON.toJSONString(res);
        }
        return JSON.toJSONString(res);
    }

//    @PostMapping("/user/place")
//    public void placeCrypt(@RequestBody InvestAdviceDTO request){
//        String investor = request.getInvestorId();
//        String type = request.getInvestType();
//        int investCount = request.getInvestNum();
//        int advice = request.getApproveAdvice();
//        investment.investByType(investor, type, investCount, advice);
//    }

    /**
     * 用户外部钱包转usdt
     * @param request: 1.外部钱包地址 2.转出金额 3.此次操作的用户id
     */
//    @PostMapping("/user/transfer")
//    public String transferUSDT(@RequestBody Map<String, Object> request){
//        // 根据用户id查询剩余usdt余额 （在钱包相关服务中做成独立接口，供前端查询，减少数据库被调用次数
//        // 两个方式：调用外部钱包暴露的接口进行转账； 或者记录到后台中，人工进行转账
//        // 等待处理结果（数据库加入了该条转账记录即为成功
//        HashMap<String, Object> res = new HashMap<>();
//        return JSON.toJSONString(res);
//    }

    /**
     * 其他币与USDT相互转换
     */
//    @PostMapping("/user/convert")
//    public String convertCurrency(@RequestBody Map<String, Object> request){
//        // 查询可用余额，根据实时价格计算可以转成多少另一种currency
//        // 调用数据库接口
//        HashMap<String, Object> res = new HashMap<>();
//        return JSON.toJSONString(res);
//    }
}
