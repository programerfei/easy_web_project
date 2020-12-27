package com.group.project.dao;

import com.group.project.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SentenceMapper {
    //发布内容
    int publicSentence(Sentence sentence);

    //根据id查询数据库发布的信息
    Sentence selectWorks(int id);

    //根据发布的时间查询发布的数据信息
    List<Sentence> selectWorksByDate(Date date);

    //根据用户名查询用户
    User selectUserByName(String username);

    //根据用户名即发布的作者查询数据信息
    List<Sentence_two> selectWorksByUsername(paramObject param);

    //根据发布的内容进行模糊查询
    List<Sentence_two> selectWorksByPro(paramObject param);

    //展示最新的二十条数据内容，用于页面的初始化内容
    List<Sentence_two> selectInitWorks(Map<String,Integer> map);

    //个人用户也可以对个人发表的数据进行删除操作
    boolean deleteSentence(Integer id);

    //查询数据库所有发布的信息数量
    long selectTotalSum();

    //查询符合用户名数据的总数
    long selectTotalByNameSum(String username);

    //查询符合关键词数据的总数
    long selectTotalBySentenceSun(String sentence);
}
