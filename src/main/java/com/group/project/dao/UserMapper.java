package com.group.project.dao;

import com.group.project.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
/**
 * 查询用户名是否存在，若存在，不允许注册
 * 注解@Param(value) 若value与可变参数相同，注解可省略
 * 注解@Results  列名和字段名相同，注解可省略
 * @param username
 * @return
 */
public interface UserMapper {

    /**
     * 查询用户名是否存在，若存在，不允许注册
     * 注解@Param(value) 若value与可变参数相同，注解可省略
     * 注解@Results  列名和字段名相同，注解可省略
     * @param username
     * @return
     */
    @Select(value = "select user.username,user.password from user where user.username=#{username}")
    @Results
            ({@Result(property = "username",column = "username"),
                    @Result(property = "password",column = "password")})
    User selectUserByName(@Param("username") String username);

    /**
     * 注册接口  插入一条user记录
     * @param user
     * @return
     */
    @Insert("insert into user(username,password,nickname,fileaddress) values(#{username},#{password},#{nickname},#{fileaddress})")
    //加入该注解可以保存对象后，查看对象插入id
    @Options(useGeneratedKeys = true,keyProperty = "username",keyColumn = "username")
    void regist(User user);

    /**
     * 登录接口
     * @param user
     * @return
     */
    @Select("select user.username from user where user.username = #{username} and password = #{password}")
    String login(User user);

}
