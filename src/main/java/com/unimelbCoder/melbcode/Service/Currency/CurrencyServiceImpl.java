package com.unimelbCoder.melbcode.Service.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import com.unimelbCoder.melbcode.cache.RedisClient;

public class CurrencyServiceImpl implements CurrencyService{

    private String COIN_PREFIX = "coin_price_";

    private static final String currency_url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,tether,cosmos,binancecoin,ripple,ethereum,dogecoin,okb&vs_currencies=usd";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Scheduled(fixedRate = 60000)
    public void getCurrencyPriceFromOuter(String currencyName, String queryLink) {
        Map<String, Map<String, Double>> response = restTemplate.getForObject(currency_url, Map.class);
        HashMap<String, Double> coin_price = new HashMap<>();

        for(Map.Entry<String, Map<String, Double>> entry: response.entrySet()){
            String coin = entry.getKey();
            Double price = entry.getValue().get("usd");
            coin_price.put(coin, price);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime rounded = now.truncatedTo(ChronoUnit.MINUTES);
        long timestamp = rounded.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;

        RedisClient.updateCoinPrice(coin_price, "coin_prices");
    }

    @Override
    public Double getCurrencyPriceFromRedis(String currencyName) {
        Map<String, Double> prices = getAllCurrencyPriceFromRedis();
        return prices.get(currencyName);
    }

    @Override
    public Map<String, Double> getAllCurrencyPriceFromRedis() {
        return RedisClient.getCoinPrices("coin_prices");
    }

    @Override
    public Double priceConverter(String cur1, String cur2, Double amount) {
        Map<String, Double> prices = getAllCurrencyPriceFromRedis();
        Double price1 = prices.get(cur1);
        Double price2 = prices.get(cur2);
        // 最多只保留五位小数
        BigDecimal bd = BigDecimal.valueOf(price1 * amount / price2).setScale(5, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
