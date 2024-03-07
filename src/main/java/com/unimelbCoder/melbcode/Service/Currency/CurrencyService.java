package com.unimelbCoder.melbcode.Service.Currency;
import java.util.Map;

public interface CurrencyService {
    /**
     * 获取币种价格 一分钟获取一次 并刷新进入缓存中
     * @param currencyName
     * @param queryLink
     */
    void getCurrencyPriceFromOuter(String currencyName, String queryLink);

    /**
     * 获取每个币种单独的价格
     * @param currencyName
     * @return
     */
    Double getCurrencyPriceFromRedis(String currencyName);

    /**
     * 获取所有货币的价格
     * @return
     */
    Map<String, Double> getAllCurrencyPriceFromRedis();

    /**
     *  币种之间的汇率转换
     * @param cur1  待转换的货币名
     * @param cur2  换算过去的货币名
     * @param amount currency1的数量
     * @return
     */
    Double priceConverter(String cur1, String cur2, Double amount);


}
