package com.unimelbCoder.melbcode.Service.User.Impl;

import com.unimelbCoder.melbcode.Service.User.UserService;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.models.dto.SimpleUserInfoDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public SimpleUserInfoDTO queryUserInfo(String username){
        User userInfo = userDao.getUserByName(username);
        return UserSimplify.toSimpleUserInfo(userInfo);
    }

    @Override
    public void userReadHistory(String userId, int articleId){
        System.out.println("adding history");
        userDao.addReadingHistory(userId, articleId);
    }

    @Override
    public User queryUserFullInfo(String userId){
        return userDao.getUserByName(userId);
    }
}
