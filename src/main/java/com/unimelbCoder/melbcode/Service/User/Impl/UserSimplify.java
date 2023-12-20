package com.unimelbCoder.melbcode.Service.User.Impl;

import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;

public class UserSimplify {

    /**
     *
     * @param user
     * @return SimpleUserInfo
     */
    public static SimpleUserInfoDTO toSimpleUserInfo(User user){
        return new SimpleUserInfoDTO().setUsername(user.getUsername())
                .setAge(user.getAge())
                .setRole(user.getRole());
    }
}
