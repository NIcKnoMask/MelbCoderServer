package com.unimelbCoder.melbcode.Service.Trade;

public interface InvestmentService {
    // TODO: 1.创建每一个币种的资金池在缓存中 2，为每一个投资的用户创建线程自动按时间进行对缓存数据的修改函数

    /**
     *
     * @param userId
     * @param currencyName
     * @param amount
     */
    void investByCurrencyName(String userId, String currencyName, int amount);

    /**
     * 按类型投资进入池子
     * @param type
     * @param userId
     * @param usdtAmount
     */
    void investByType(String type, String userId, int usdtAmount);

    /**
     * 是否允许自动切换投资金额等
     * @param userId
     * @param approve
     */
    void approveAutoTrade(String userId, boolean approve);

}
