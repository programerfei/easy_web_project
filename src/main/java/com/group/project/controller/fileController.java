package com.group.project.controller;


import com.group.project.dao.InfoMapper;
import com.group.project.pojo.Result;
import com.group.project.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

//上传文件接口

@CrossOrigin(origins = {"http://localhost:8080", "null"})
@RestController
public class fileController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/uploadfile")
    @ResponseBody
    public Result uploadFile(MultipartFile file) throws IOException {
        Result result=new Result();
        result.setSuccess(false);
        result.setDetail(null);

//        String path = ResourceUtils.getURL("classpath:").getPath()+"static/images/";

//
//        // 图片存储目录及图片名称
//        String url_path = "/images/" +  fileName;
//
//        //System.out.println("图片保存地址："+savePath);
//        // 访问路径=静态资源路径+文件目录路径
//        String visitPath = url_path;
//        //System.out.println("图片访问uri："+visitPath);
//
//        //图片保存路径
//        String savePath = path;
//        //若没有路径，则新创建
//        File saveFile = new File(savePath);
//        if (!saveFile.exists()){
//            saveFile.mkdirs();
//        }


        //因为我们无法操作jar包内容，所以我们只能将文件存放在别的位置，与jar包同级的目录是一个不错的选择。
        //1.首先获取根目录
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!path.exists()) {
            path = new File("");
        }

        //获取文件名 加入时间戳防止重名
        String fileName = System.currentTimeMillis()+file.getOriginalFilename();

        //2.然后获取需要的目录，我们设定我们需要将文件存放在与jar包同级的static的upload目录下
        File upload = new File(path.getAbsolutePath(),"/static/upload/");
        if(!upload.exists()) {
            upload.mkdirs();
        }

        //3.然后当我们要将上传的文件存储的时候，按照下面的方式去新建文件，然后使用你喜欢的方式进行存储。
        File uploadfile = new File(path.getAbsolutePath(),"/static/upload/"+fileName);


        result.setMsg("文件上传成功");
        result.setSuccess(true);
        result.setDetail(fileName);
        file.transferTo(uploadfile);
//        result.setMsg("文件上传成功");
//        result.setSuccess(true);
//        result.setDetail(userService.selectUrl());

//        File localFile=new File(savePath+fileName);
//        try {
//            file.transferTo(localFile);  //将临时存储的文件移动到真实存储路径下
//            result.setMsg("文件上传成功");
//            result.setSuccess(true);
//            result.setDetail(visitPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return result;
    }

    //向前端返回头像url
    @GetMapping(value = "/getuserurl")
    public String selectUserUrl(String username){
        return userService.selectUrl(username);
    }
}
