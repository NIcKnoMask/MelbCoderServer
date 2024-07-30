//package com.unimelbCoder.melbcode.Controller;
//
//
//import com.alibaba.fastjson.JSON;
//import com.unimelbCoder.melbcode.Service.Currency.CurrencyServiceImpl;
//import com.unimelbCoder.melbcode.Service.Wallet.WalletServiceImpl;
//import com.unimelbCoder.melbcode.bean.User;
//import com.unimelbCoder.melbcode.utils.JwtUtils;
//import org.apache.ibatis.annotations.Param;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//public class CryptoController {
//
//    @Autowired
//    private CurrencyServiceImpl currencyService;
//
//    @Autowired
//    private WalletServiceImpl walletService;
//
//    @Autowired
//    JwtUtils jwtUtils;
//
//    @GetMapping("/cryptoService/price/all_prices")
//    public String getCryptoPrices(){
//        Map<String, Double> price_map = currencyService.getAllCurrencyPriceFromRedis();
//        Map<String, Object> res = new HashMap<>();
//        if(price_map != null){
//            res.put("data", price_map);
//            res.put("flag", "ok");
//        }else{
//            res.put("flag", "error");
//        }
//        return JSON.toJSONString(res);
//    }
//
//    @GetMapping("/cryptoService/price/{cryptoName}")
//    public String getCryptoPriceById(@PathVariable String cryptoName){
//        Double price = currencyService.getCurrencyPriceFromRedis(cryptoName);
//        Map<String, Object> res = new HashMap<>();
//        if(price != null){
//            res.put("data", price);
//            res.put("flag", "ok");
//        }else{
//            res.put("flag", "error");
//        }
//        return JSON.toJSONString(res);
//    }
//
//    @PostMapping("/cryptoService/invest")
//    public String investOperation(@RequestBody Map<String, Object> map,
//                               @RequestHeader (name = "Authorization", required = false) String token){
//        if (token == null && jwtUtils.isTokenExpired(token)) {
//            return "login";
//        }
//        // 是否可以替换成只获取简化用户信息
//        User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);
//        String operation = map.get("operation").toString();
//        String crypto_id = map.get("crypto").toString();
//        Double amount = Double.parseDouble(map.get("amount").toString());
//        Map<String, Object> operation_result = null;
//        if(operation.equals("deposit")){
//            operation_result = currencyService.investCoinToMainPool(userInfo.getId(), crypto_id, amount);
//            //可以有其他业务
//        }else if(operation.equals("withdraw")){
//            operation_result = currencyService.withdrawCoinFromMainPool(userInfo.getId(), crypto_id, amount);
//        }
//
//        Map<String, Object> res = new HashMap<>();
//        res.put("flag", operation_result.get("status"));
//        Map<String, String> data = new HashMap<>();
//        data.put("wallet-balance", operation_result.get("walletBalance").toString());
//        data.put("invest-balance", operation_result.get("investBalance").toString());
//        res.put("data",data);
//
//        return JSON.toJSONString(res);
//    }
//}
