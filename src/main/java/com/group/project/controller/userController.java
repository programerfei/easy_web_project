package com.group.project.controller;

import com.group.project.pojo.Result;
import com.group.project.pojo.User;
import com.group.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//跨域问题解决方法
@CrossOrigin(origins = {"http://localhost:8080", "null"})
@RestController
public class userController {
    @Autowired
    private UserService userService;
    /**
     * 注册
     * @param user 参数封装
     * @return Result
     */
    @GetMapping(value = "/regist")
    public Result regist(User user){
        return userService.regist(user);
    }
    /**
     * 登录
     * @param user 参数封装
     * @return Result
     */
    @GetMapping(value = "/login")
    public Result login(User user){
        return userService.login(user);
    }

    @GetMapping(value = "/update")
    public Result update(User user){
        return userService.updateUser(user);
    }
}
