package com.unimelbCoder.melbcode.models.dao;

import org.apache.ibatis.annotations.Param;

public interface WalletDao {
    public double getCurrencyAmount(@Param("userid") String userId, @Param("currency") String currency);

}
