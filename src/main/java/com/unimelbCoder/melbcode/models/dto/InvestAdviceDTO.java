package com.unimelbCoder.melbcode.models.dto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InvestAdviceDTO {
    /**
     * 投资者：userId
     */
    private String investorId;

    /**
     * 投资类型：risky, normal, conserved
     */
    private String investType;

    /**
     * 投资的ustd数量
     */
    private int investNum;

    /**
     * 是否允许AI自动调整投资金额: 0不允许，1允许
     */
    private int approveAdvice;
}
