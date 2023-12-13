package com.unimelbCoder.melbcode.Controller;

import com.unimelbCoder.melbcode.bean.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @RequestMapping("/login")
    public String login(){
        System.out.println("this function has been called");
        return "ok";
    }
}
