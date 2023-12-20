package com.unimelbCoder.melbcode.models.dto;


import com.unimelbCoder.melbcode.models.dao.UserDao;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RankItemDTO {
    /**
     * 排名
     */
    private Integer rank;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 用户
     */
    private SimpleUserInfoDTO user;
}
