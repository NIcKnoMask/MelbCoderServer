package com.unimelbCoder.melbcode.Service.Wallet;

public interface WalletService {

    /**
     * @param userId    查询用户的id
     * @param currencyName  用户要查询的代币的名字
     */
    Double queryBalance(String userId, String currencyName);

    /**
     * cur1 -> cur2
     * @param cur1  待转币种
     * @param cur2  需要转过去的币种
     * @param amount    待转币种的数量
     * @return  true: 转换成功 false: 转换失败
     */
    boolean transferCurrency(String cur1, String cur2, int amount);

    /**
     * 向钱包中充值USDT。生成一个钱包地址和ref号给用户 收到用户的转账后即会向钱包中充值
     * @param amount    充值金额
     * @return
     */
    String transferIntoUsdt(int amount, String userId);

    /**
     * 向钱包提现 用户需要提供外部钱包的收账地址
     * @param amount    提现金额
     * @param outerAddress  外部钱包 如欧易等地址
     */
    void usdtCheckOut(int amount, String outerAddress, String userId);

    /**
     * 收到对方的usdt转账后调用此接口给对方的usdt中增加对应金额
     * @param amount
     * @param userId
     */
    void addUsdt(int amount, String userId);


}
