package com.unimelbCoder.melbcode.Service.User.Impl;

import com.unimelbCoder.melbcode.Service.User.UserService;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public SimpleUserInfoDTO queryUserInfo(String username){
        User userInfo = userDao.getUserByUsername(username);
        return UserSimplify.toSimpleUserInfo(userInfo);
    }
}
