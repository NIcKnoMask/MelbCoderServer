package com.unimelbCoder.melbcode.models.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CurrencyDTO {
    /**
     * 自增的id
     */
    private int currencyId;

    /**
     * 货币的代号
     */
    private String currencyName;

    /**
     * 货币的简介
     */
    private String currencyIntro;

    /**
     * 获取货币实时价格的链接
     */
    private String priceAcqLink;
}
