package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.Service.Trade.InvestmentImpl;
import com.unimelbCoder.melbcode.Service.User.Impl.UserServiceImpl;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.InvestAdviceDTO;
import com.unimelbCoder.melbcode.models.dto.MessageItemDTO;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserDao userDao;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private InvestmentImpl investment;

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

    @PostMapping("/user/place")
    public void placeCrypt(@RequestBody InvestAdviceDTO request){
        String investor = request.getInvestorId();
        String type = request.getInvestType();
        int investCount = request.getInvestNum();
        int advice = request.getApproveAdvice();
        investment.investByType(investor, type, investCount, advice);
    }

    /**
     * 用户外部钱包转usdt
     * @param request: 1.外部钱包地址 2.转出金额 3.此次操作的用户id
     */
    @PostMapping("/user/transfer")
    public String transferUSDT(@RequestBody Map<String, Object> request){
        // 根据用户id查询剩余usdt余额 （在钱包相关服务中做成独立接口，供前端查询，减少数据库被调用次数
        // 两个方式：调用外部钱包暴露的接口进行转账； 或者记录到后台中，人工进行转账
        // 等待处理结果（数据库加入了该条转账记录即为成功
        HashMap<String, Object> res = new HashMap<>();
        return JSON.toJSONString(res);
    }

    /**
     * 其他币与USDT相互转换
     */
    @PostMapping("/user/convert")
    public String convertCurrency(@RequestBody Map<String, Object> request){
        // 查询可用余额，根据实时价格计算可以转成多少另一种currency
        // 调用数据库接口
        HashMap<String, Object> res = new HashMap<>();
        return JSON.toJSONString(res);
    }
}
