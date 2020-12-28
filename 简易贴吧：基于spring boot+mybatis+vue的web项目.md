# 简易贴吧：基于spring boot+mybatis+vue的web项目

## 一、项目简介

这是一个简单的贴吧项目，主要用于用户发帖交流。

使用前后端分离：spring boot，mybatis+vue，也是现在使用最广的框架。

源码，GitHub：https://github.com/programerfei/easy_web_project/tree/master

这个项目主要是学习和了解前后端的具体操作，通过一个简单的项目来熟悉自己的技术栈。

项目入口：http://121.196.154.219:8080/pages/index.html

## 二、技术点

spring boot

mybatis

vue

## 三、注意事项

我上传到GitHub上的项目代码主要是后端的代码，但是在resources下的静态资源static目录下放好了前端打包好的项目。

其次，在spring boot的配置文件中，我为了自己服务器的安全以将配置文件中的服务器IP地址和服务器数据库的密码做了处理，如果有人需要则需要将配置文件改一下。

**注：** 在项目的最后一个阶段，我们在本地前后端项目接口对接测试完整之后，在整合了前端项目，将最后的项目打jar包然后将其丢在服务器之上，我们访问项目然后进行测试，但是此时我们发现项目的头像上传功能是不成功的。

原因在于当我们把jar包放在服务器上之后，我们不能正常访问jar包下的静态资源。具体解决方式在最后说明，此处大概提一下。

## 四、后端实现

首先是项目结构

![image-20201228183452474](C:\Users\11499\AppData\Roaming\Typora\typora-user-images\image-20201228183452474.png)

因为此项目是一个很基本的CRUD的普通项目，所以项目的核心代码便是CRUD代码。

我们此项目使用了mybatis框架实现对数据库的操作，所以在此项目中我使用了注解和mapper.xml配置文件两种方式进行开发。

**-** 首先是用户的登陆注册功能

登录注册功能我们使用的是注解开发的方式，核心代码如下：

![image-20201228184446099](C:\Users\11499\AppData\Roaming\Typora\typora-user-images\image-20201228184446099.png)

如图所示，注册使用的注解便是插入的sql语句，登录接口使用的注解便是查询的sql语句，在此之前，我们还实现了一个查询检查是否用户名是否注册过。

**-** 然后是搜索和初始化的功能

```xml
  <!--    根据发布的内容进行查询-->
    <!--    同时返回用户的头像-->
    <select id="selectWorksByPro" parameterType="paramObject" resultType="com.group.project.pojo.Sentence_three">
        select works.id,works.username,user.nickname,user.signature,user.introductory,fileaddress,works.production,works.date
        from user,works
        where user.username=works.username and works.username!=#{targetObject} and production
        like concat('%',#{targetObject},'%')
        order by works.date desc
        limit #{startIndex},#{pageSize}
    </select>

<!--    初始化页面展示的句子，每页20条根据发布的最新数据-->
<!--    此处返回对象的头像地址-->
    <select id="selectInitWorks" parameterType="map" resultType="com.group.project.pojo.Sentence_three">
        select works.id,works.username,user.nickname,user.signature,user.introductory,fileaddress,works.production,works.date
        from user,works
        where user.username=works.username
        order by works.date desc
        limit #{startIndex},#{pageSize}
    </select>
```

此处的按用户名查询和页面初始的xml文件内容上，模糊查询，分页查询，联表查询。

**-** 最后， 此项目还有一个非常主要的功能便是头像上传。

因为考虑到项目不是很大，所以文件上传我们的实现方式便是：前端发来文件请求，然后我们将其存入项目的静态资源static下，饭后将静态资源返回给前端项目，此时前端将用户名，静态资源的url返回后端并存入数据库中。此后我们对用户的返回结果加上数据库中的url便能正确展示用户的头像。



**-**当然，这只是在本地项目中，但是我们打jar包并部署到服务器上就不是这种情况了，达成jar包，我们此时便不能正确上传头像了。此时运行jar包会将上传的文件存入服务器的某一个文件目录下。这样我们我们便不能正常访问服务器下某一文件的图片文件。



我们的解决方式便是我们在服务器jar包的同级目录下创建一个文件夹，将其配置成静态资源，这样，我们便能直接访问这个静态资源的文件，整个项目也就能正常运行了。

properties的文件配置如下

```properties
#上传文件的配置
#单文件限制大小
spring.http.multipart.maxFileSize=10MB
#一次请求限制大小
spring.http.multipart.maxRequestSize=500MB

#你需要在application.yml配置中加入以下代码，指定两个静态资源的目录，这样你上传的文件就能被外部访问到了。
spring.web.resources.static-locations=classpath:static/,file:static/

server.tomcat.basedir=static/tomcat
```

文件上传的接口内容如下（emm,因为功能也相对简单，自己也懒，所以将文件上传代码写入接口了，具体应该写在service包下的某一实现类中）

```java
@PostMapping(value = "/uploadfile")
    @ResponseBody
    public Result uploadFile(MultipartFile file) throws IOException {
        Result result=new Result();
        result.setSuccess(false);
        result.setDetail(null);

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

        return result;
```

然后就是接口了（一部分），大家都可以访问

```Java
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
```

## 五、项目预览

主页：

![image-20201228191811069](C:\Users\11499\AppData\Roaming\Typora\typora-user-images\image-20201228191811069.png)

![image-20201228191757790](C:\Users\11499\AppData\Roaming\Typora\typora-user-images\image-20201228191757790.png)

也是自己和舍友共同努力的结果了。