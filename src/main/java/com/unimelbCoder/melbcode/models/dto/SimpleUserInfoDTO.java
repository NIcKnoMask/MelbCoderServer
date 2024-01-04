package com.unimelbCoder.melbcode.models.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SimpleUserInfoDTO {
    private static final long serialVersionUID = 4802653694786272120L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 年龄
     */
    private int age;

    /**
     * 角色
     */
    private String role;

    /**
     * 个人简介
     */
    private String introduction;
}
