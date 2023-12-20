package com.unimelbCoder.melbcode.Service.User;

import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;

public interface UserService {
    /**
     * 获取登录的用户信息,并更行丢对应的ip信息
     *
     * @param username  用户独特id
     * @return 返回用户基本信息
     */
    SimpleUserInfoDTO queryUserInfo(String username);

}
