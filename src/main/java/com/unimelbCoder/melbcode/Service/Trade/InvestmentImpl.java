package com.unimelbCoder.melbcode.Service.Trade;

import com.unimelbCoder.melbcode.cache.RedisClient;

public class InvestmentImpl {
    private static final String PREFIX = "invest_";


    public boolean investByType(String investor, String type, int usdt, int approve){
        // 从redis中查询该用户是否有投资记录
        // 根据公式计算出每个币种应该投资的数量
        // 修改redis中的投资数额
        // 修改redis的中心池金额大小
        // 计算并修改用户每个已投资币种的挖掘速率
        if(RedisClient.isUserInvested(PREFIX + investor)){

        }

        switch (type){
            case "RISKY":
                System.out.println("hiii");
                break;
            case "NORMAL":
                System.out.println("this is normal invest");
                break;
            case "CONSERVE":
                System.out.println("this is converse invest");
                break;
        }
        return true;
    }
}
