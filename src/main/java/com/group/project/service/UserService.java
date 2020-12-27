package com.group.project.service;

import com.group.project.dao.InfoMapper;
import com.group.project.dao.UserMapper;
import com.group.project.pojo.Result;
import com.group.project.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InfoMapper infoMapper;
    /**
     * 注册
     * 同时设置一个默认头像
     * @param user 参数封装
     * @return Result
     */
    public Result regist(User user) {
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        String ipString="http://192.168.1.104:8080";
        try {
            User existUser = userMapper.selectUserByName(user.getUsername());
            if(existUser != null){
                //如果用户名已存在
                result.setMsg("用户名已存在");
            }else{
                user.setFileaddress(ipString+"/images/9a504fc2d5628535d9c7a3e597ef76c6a6ef63ca.jpg");
                userMapper.regist(user);
                result.setMsg("注册成功");
                result.setSuccess(true);
                result.setDetail(user);
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 登录
     * @param user 用户名和密码
     * @return Result
     */
    public Result login(User user) {
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        String username=null;
        try {
            username= userMapper.login(user);
            if(username == null){
                result.setMsg("用户名或密码错误");
            }else{
                User exitGetUser=infoMapper.selectUserByUpdate(username);
                result.setMsg("登录成功");
                result.setSuccess(true);
                //user.setUsername(username);
                result.setDetail(exitGetUser);
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 编辑用户信息
     * 此处需要将头像存进数据库
     * @param user
     * @return Result
     */
    public Result updateUser(User user){
        Result result = new Result();
        infoMapper.updateUserInfo(user);
        //查询被修改后的信息

        result.setMsg("修改信息成功");
        result.setSuccess(true);
        result.setDetail(user);
        return result;
    }

    public String selectUrl(String username){

        return "";
    }
}
