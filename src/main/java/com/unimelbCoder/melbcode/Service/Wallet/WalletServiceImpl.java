package com.unimelbCoder.melbcode.Service.Wallet;

import com.unimelbCoder.melbcode.models.dao.WalletDao;

public class WalletServiceImpl implements WalletService{
    private WalletDao walletDao;

    @Override
    public Double queryBalance(String userId, String currencyName){
        return walletDao.getCurrencyAmount(userId, currencyName);
    }

    @Override
    public boolean transferCurrency(String cur1, String cur2, int amount){
        // 去redis中获取货币的
        // 修改数据库钱包中对应货币的数量
        return true;
    }

    @Override
    public String transferIntoUsdt(int amount, String userId){
        return "";
    }

    @Override
    public void usdtCheckOut(int amount, String outerAddress, String userId) {

    }

    @Override
    public void addUsdt(int amount, String userId) {

    }

    public Double investCurrency(String userId, String currencyName, Double amount){
        // 需要先查询一次余额
        return 0.0;
    }

    public Double withdrawCurrency(String userId, String currencyName, Double amount){
        // 与上述方法其实重复 在业务层也可以调用上述方法，将amount改为-amount，则为当前方法
        return 0.0;
    }
}
