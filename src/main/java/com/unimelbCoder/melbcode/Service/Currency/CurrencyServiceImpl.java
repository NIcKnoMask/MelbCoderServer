//package com.unimelbCoder.melbcode.Service.Currency;
//
//import com.unimelbCoder.melbcode.Service.Common.TokenBucketService;
//import com.unimelbCoder.melbcode.Service.Wallet.WalletServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.HashMap;
//import java.util.Map;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//import java.util.concurrent.locks.ReentrantLock;
//
//import com.unimelbCoder.melbcode.cache.RedisClient;
//
//public class CurrencyServiceImpl implements CurrencyService{
//
//    private final String COIN_PREFIX = "coin_price_";
//
//    private final String INVEST_COIN_PREFIX = "invest_";
//
//    private static final String currency_url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,tether,cosmos,binancecoin,ripple,ethereum,dogecoin,okb&vs_currencies=usd";
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    private final TokenBucketService tokenBucketService = new TokenBucketService();
//
//    private final WalletServiceImpl walletService = new WalletServiceImpl();
//
//    private final ReentrantLock lock = new ReentrantLock();
//
//    @Override
//    @Scheduled(fixedRate = 60000)
//    public void getCurrencyPriceFromOuter(String currencyName) {
//        Map<String, Map<String, Double>> response = restTemplate.getForObject(currency_url, Map.class);
//        HashMap<String, Double> coin_price = new HashMap<>();
//
//        for(Map.Entry<String, Map<String, Double>> entry: response.entrySet()){
//            String coin = entry.getKey();
//            Double price = entry.getValue().get("usd");
//            coin_price.put(coin, price);
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime rounded = now.truncatedTo(ChronoUnit.MINUTES);
//        long timestamp = rounded.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
//
//        RedisClient.updateCoinPrice(coin_price, "coin_prices");
//    }
//
//    @Override
//    public Double getCurrencyPriceFromRedis(String currencyName) {
//        Map<String, Double> prices = getAllCurrencyPriceFromRedis();
//        return prices.get(currencyName);
//    }
//
//    @Override
//    public Map<String, Double> getAllCurrencyPriceFromRedis() {
//        return RedisClient.getCoinPrices("coin_prices");
//    }
//
//    @Override
//    public Double priceConverter(String cur1, String cur2, Double amount) {
//        Map<String, Double> prices = getAllCurrencyPriceFromRedis();
//        Double price1 = prices.get(cur1);
//        Double price2 = prices.get(cur2);
//        // 最多只保留五位小数
//        BigDecimal bd = BigDecimal.valueOf(price1 * amount / price2).setScale(5, RoundingMode.HALF_UP);
//        return bd.doubleValue();
//    }
//
//    public Map<String, Object> investCoinToMainPool(String userId, String coinType, Double amount){
//        Map<String, Object> res = new HashMap<>();
//        String status = "success";
//
//        // 限制流量，需要先获取令牌才能进行操作
//        if(tokenBucketService.tryGetToken()){
//            try{
//                lock.lock();
//                // 检查钱包中是否还有余额
//                Double balance = walletService.queryBalance(userId, coinType);
//                if(balance < amount){
//                    status = "fail";
//                }else{
//                    // 将余额从钱包扣除
//                    Double walletBalance = walletService.investCurrency(userId, coinType, amount);
//                    res.put("walletBalance", walletBalance);
//                    // 更新redis中的投资情况
//                    Map<String, Object> redisRes = RedisClient.investCoin(userId, coinType, amount);
//                    res.put("investBalance", redisRes.get("balance"));
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//                lock.unlock();
//            }
//        }else{
//            // 获取令牌失败，请求会被拒绝
//            status = "fail";
//        }
//        res.put("status", status);
//        return res;
//    }
//
//    public Map<String, Object> withdrawCoinFromMainPool(String userId, String coinType, Double amount){
//        Map<String, Object> res = new HashMap<>();
//        String status = "success";
//        if(tokenBucketService.tryGetToken()){
//            try{
//                lock.lock();
//                // 将余额从钱包扣除
//                Double walletBalance = walletService.withdrawCurrency(userId, coinType, amount);
//                res.put("walletBalance", walletBalance);
//                // 更新redis中的投资情况
//                Map<String, Object> redisRes = RedisClient.withdrawCoin(userId, coinType, amount);
//                if(redisRes.get("status").equals("fail")){
//                    status = "fail";
//                }else{
//                    res.put("balance", redisRes.get("balance"));
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//                lock.unlock();
//            }
//        }else{
//            status = "fail";
//        }
//        res.put("status", status);
//        return res;
//    }
//}
