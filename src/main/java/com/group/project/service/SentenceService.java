package com.group.project.service;

import com.group.project.dao.SentenceMapper;
import com.group.project.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class SentenceService {
    @Autowired
    private SentenceMapper sentenceMapper;


    /**
     * 发布具体内容信息
     */
    public Result publicSentenceWorks(Sentence sentence){
        Result result=new Result();
        result.setSuccess(false);
        result.setDetail(null);

        //获取发布数据这一时刻的具体时间
        try{
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());

            sentence.setDate(date);
            sentenceMapper.publicSentence(sentence);

            result.setMsg("发布内容成功");
            result.setSuccess(true);
            int senID=sentence.getId();

            Sentence exitSentence=sentenceMapper.selectWorks(senID);
            result.setDetail(exitSentence);
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据数据id查询
     * 暂时没有使用
     */
    public Result selectSenById(int id){
        Result result=new Result();
        result.setSuccess(false);
        result.setDetail(null);
        int findId=0;

        try{
            Sentence exitSentence = sentenceMapper.selectWorks(id);
            if(exitSentence==null){
                result.setMsg("id 数据错误，请检查id");
            }else{
                result.setMsg("数据查询成功");
                result.setDetail(exitSentence);
            }
        }catch (Exception e){
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据数据发布作者即用户名username查询
     * 返回一个包含多数Result的list数据
     */
    public Result_two<List<Sentence_three>> selectSenByUsername(String username,Integer currentPage){
        Result_two result=new Result_two();
        result.setSuccess(false);
        result.setDetail(null);

        List<Sentence_two> list=new ArrayList<>();
        int pageSize = 20;  //每页显示几个

        paramObject param=new paramObject();
        param.setTargetObject(username);
        param.setStartIndex((currentPage-1)*pageSize);
        param.setPageSize(pageSize);
        list=sentenceMapper.selectWorksByUsername(param);

        long length= sumPage_two(username);//所有数据的数量

        User tempUser=sentenceMapper.selectUserByName(username);
        if(tempUser==null){
            result.setMsg("本平台没有该用户，建议先进行注册");
            result.setSuccess(false);
            result.setPageNumber(0);
        }else{
            if(list.isEmpty()){
                result.setMsg("没有该用户的内容");
                result.setSuccess(false);
                result.setPageNumber(0);
            }else{
                result.setMsg("该用户的数据查询成功");
                result.setSuccess(true);
                long pageLength=0;
                if(length%20==0){
                    pageLength=length/pageSize;
                }else{
                    pageLength=length/pageSize+1;//符合要求的页数
                }
                result.setPageNumber(pageLength);
                result.setDetail(list);
            }
        }
        return result;
    }

    /**
     * 根据发布内容模糊查询
     * 此处也要进行分页查询
     * 返回的页数
     * 参数 sentence
     */
    public Result_two<List<Sentence_three>> selectSenByWorks(String production,Integer currentPage){
        Result_two result=new Result_two();
        result.setSuccess(false);
        result.setDetail(null);

        List<Sentence_two> list=new ArrayList<>();
        int pageSize = 20;  //每页显示几个数据

        paramObject param=new paramObject();
        param.setTargetObject(production);
        param.setStartIndex((currentPage-1)*pageSize);
        param.setPageSize(pageSize);
        list=sentenceMapper.selectWorksByPro(param);

        long length= sumPage_three(production);//所有数据的数量

        if(list.isEmpty()){
            result.setMsg("没有查到该关键字的相关数据");
            result.setSuccess(false);
            result.setPageNumber(0);
        }else{
            result.setMsg("该关键字的数据查找成功");
            result.setSuccess(true);
            long pageLength=0;
            if(length%20==0){
                pageLength=length/pageSize;
            }else{
                pageLength=length/pageSize+1;//符合要求的页数
            }
            result.setPageNumber(pageLength);
            result.setDetail(list);
        }
        return result;
    }

    /**
     * 根据数据发布时间查询
     * c此处模糊查询
     * 返回一个包含多数Result的list数据
     */
    public Result<List<Sentence>> selectSenByDate(Date date){
        Result result=new Result();
        result.setSuccess(false);
        result.setDetail(null);

        List<Sentence> list=new ArrayList<>();
        //判断此事件点是否存在发布的信息
        list=sentenceMapper.selectWorksByDate(date);
        if(list.isEmpty()){
            result.setMsg("没有此时刻发布的数据");
        }else{
            result.setMsg("此时刻发表的内容查询成功");
            result.setSuccess(true);
            result.setDetail(list);
        }
        return result;
    }


    /**
     * 初始化时返回页面的总数
     * @return int,页面数量
     */
    public long sumPage(){
        long length=0;
        length=sentenceMapper.selectTotalSum();
        return length;
    }

    /**
     * 根据用户名搜索时返回页面的总数
     * @return int,页面数量
     */
    public long sumPage_two(String username){
        long length=0;
        length=sentenceMapper.selectTotalByNameSum(username);

        return length;
    }

    /**
     * 根据发布信息关键字搜索时返回页面的总数
     * @return int,页面数量
     */
    public long sumPage_three(String sentence){
        long length=0;
        length=sentenceMapper.selectTotalBySentenceSun(sentence);
        return length;
    }

    /**
     * 初始化页面的展示数据
     * 初始化展示最新发布的二十条数据
     */
    public Result_two selectAllWorks(int currentPage){
        Result_two result=new Result_two();
        result.setSuccess(false);
        result.setDetail(null);

        List<Sentence_two> list=new ArrayList<>();
//        int currentPage = 1;  //第几页
        int pageSize = 20;  //每页显示几个
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("startIndex",(currentPage-1)*pageSize);
        map.put("pageSize",pageSize);
        list=sentenceMapper.selectInitWorks(map);

        long length= sumPage();//所有数据的数量
        long pageLength=0;
        if(length%20==0){
            pageLength=length/pageSize;
        }else{
            pageLength=length/pageSize+1;//符合要求的页数
        }
        result.setPageNumber(pageLength);

        if(list.isEmpty()){
            result.setMsg("平台暂时没有展示的数据");
            result.setPageNumber(0);
        }else{
            result.setMsg("展示最新的数据");
            result.setSuccess(true);
            result.setDetail(list);
        }
        return result;
    }


    /**
     * 在用户主页删除该用户的发布数据
     */
    public Result deleteUserWorks(Integer id){
        Result result=new Result();
        result.setSuccess(false);
        result.setDetail(null);

        //根据数据的主键ID进行标识删除
        sentenceMapper.deleteSentence(id);

        //需要加逻辑判断是否将数据删除成功。

        result.setMsg("该条数据删除成功");
        result.setSuccess(true);
        result.setDetail(null);

        return result;
    }
}
