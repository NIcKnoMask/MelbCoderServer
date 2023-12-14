package com.unimelbCoder.melbcode.Controller;

import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    UserDao userDao;

    @RequestMapping("/login")
    public String login(@RequestBody User user){
        System.out.println("this function has been called");
        System.out.println(user);
        User us = userDao.getUserByMessage(user.getUsername(), user.getPassword());
        System.out.println(us);
        return "ok";
    }
}
