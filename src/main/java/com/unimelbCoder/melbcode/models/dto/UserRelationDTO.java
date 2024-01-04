package com.unimelbCoder.melbcode.models.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRelationDTO {

    /**
     * 主用户名
     */
    private String mainUserId;

    /**
     * 关注的用户名
     */
    private String followUserId;

    /**
     * 关注状态：1-正在关注 2-取消关注
     */
    private Integer followState;
}
