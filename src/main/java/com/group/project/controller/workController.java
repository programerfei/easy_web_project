package com.group.project.controller;

import com.group.project.pojo.*;
import com.group.project.service.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:8080", "null"})
@RestController
public class workController {
    @Autowired
    SentenceService sentenceService;

    //发布内容接口
    @PostMapping(value="/updateworks")
    public Result updateSentence(Sentence sentence){
        return sentenceService.publicSentenceWorks(sentence);
    }

    //根据id查询数据接口
    @GetMapping(value = "/findbyid")
    public Result selectSentenceById(int id){
        return sentenceService.selectSenById(id);
    }

    //根据用户名即作者查询数据接口
    @GetMapping(value = "/findbyusername")
    public Result_two<List<Sentence_three>> selectSentenceByUsername(String username, Integer currentPage){
        return sentenceService.selectSenByUsername(username,currentPage);
    }

    //根据时间查询数据内容接口
    @GetMapping(value = "/findbydate")
    public Result<List<Sentence>> selectSentenceByDate(Date date){
        return sentenceService.selectSenByDate(date);
    }

    //根据用户关键字模糊查询接口
    @GetMapping(value = "/findbysen")
    public Result_two<List<Sentence_three>> selectSentenceBySentence(String targetParam,Integer currentPage){
        return sentenceService.selectSenByWorks(targetParam,currentPage);
    }

    //初始化页面，展示最新的二十条数据
    @GetMapping(value = "/findinit")
    public Result_two<List<Sentence_three>> selectSentenceByInit(Integer currentPage){
        return sentenceService.selectAllWorks(currentPage);
    }

    //返回包含用户头像的所有信息

    //根据用户名判断是否是自己的主页
    //根据id进行标识删除数据
    @GetMapping(value = "/deletesen")
    public Result deleteSentenceById(Integer id){
        return sentenceService.deleteUserWorks(id);
    }
}
