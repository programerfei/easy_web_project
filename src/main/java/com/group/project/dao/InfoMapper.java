package com.group.project.dao;

import com.group.project.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface InfoMapper {
    //更具用户名查询用户的所有信息
    User selectUserByUpdate(String username);

    //修改用户信息
    int updateUserInfo(User user);

    //根据用户名查询头像地址
    String selectUrlByUsername(String username);
}
