package com.unimelbCoder.melbcode.models.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WalletDTO {
    /**
     * 主键 映射到user表
     */
    private String userId;

    /**
     * 比特币所有的数量
     */
    private float bitcoin;

    /**
     * 其他货币拥有的数量
     */

    private float usdt;

    private float eth;

    private float doge;

    private float mars;

    private float okb;

    private float atom;

    private float bnb;

    private float xrp;
}
