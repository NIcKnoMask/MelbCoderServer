package com.unimelbCoder.melbcode.Controller;

import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.utils.NotifyMsgEvent;
import com.unimelbCoder.melbcode.models.enums.NotifyTypeEnum;
import com.unimelbCoder.melbcode.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {
    @Autowired
    UserDao userDao;

    @RequestMapping("/signup")
    public String signup(@RequestBody User user){
        System.out.println("user ask for sign up");
        User us = userDao.getUserByName(user.getUsername());
        if(us != null){
            System.out.println("already exist username");
            return "failed";
        }
        userDao.createUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(),
                user.getAge());
        System.out.println("successfully insert the row");
        return "ok";
    }

    /**
     * 用户注册完毕之后触发事件
     *
     * @param username
     */
    private void processAfterRegister(String username){
        SpringUtils.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.REGISTER, username));
    }
}
