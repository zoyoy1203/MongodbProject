package com.mongodb;

import com.mongodb.entity.*;
import com.mongodb.util.CookieUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = {"insertTest"})
    public void iTest() throws IllegalArgumentException, IllegalAccessException {
        for(int i=0;i<1000;i++){
            User user = new User();
            user.setNickname("测试"+i);
            user.setPassword(CookieUtils.md5Encrypt(String.valueOf(123)));
            user.setUsername("测试"+i);
            user.setMotto("该用户比较懒哦~");
            user.setAvatar("favicon.jpg");
            System.out.println(user);
            mongoTemplate.save(user);
        }

    }


    private User getMine(HttpServletRequest request, HttpServletResponse response){
        String username = (String) request.getSession().getAttribute("user");
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query,User.class);
        return user;
    }

    // 个人中心
    @RequestMapping(value = {"","/","/index"})
    public String index(HttpServletRequest request, HttpServletResponse response) {
        String username = (String) request.getSession().getAttribute("user");
        if(username != null){
            User user = getMine(request,response);
            request.setAttribute("myInfo", user);
            request.setAttribute("avatar",user.getAvatar());
            return "/index";
        }else{
            request.setAttribute("errorMsg", "请先登录！");
            return "/login";
        }
    }

    // 注册
    @RequestMapping(value = {"register"})
    public String register(){
        return "/register";
    }
    @RequestMapping(value = {"saveUser"})
    public String saveUser(Model model, @RequestParam String nickname,
    @RequestParam String username, @RequestParam String password){
        User user = new User();
        user.setNickname(nickname);
        user.setPassword(CookieUtils.md5Encrypt(password));
        user.setUsername(username);
        user.setMotto("该用户比较懒哦~");
        user.setAvatar("favicon.jpg");
        mongoTemplate.save(user);
        return "/login";
    }

    // 登录
    @RequestMapping(value = {"login"})
    public String login(){
        return "/login";
    }
    @RequestMapping(value = {"userLogin"})
    public void userLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //设置字符集
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 检测表单是否重复提交
        String token=request.getParameter("token");
        HttpSession session=request.getSession();
        String sessionUuid=(String)session.getAttribute("uuid");
        session.removeAttribute("uuid");
        if(token.equals(sessionUuid)) {
            System.out.println("合法的请求，是第一次提交表单");
        }else {
            System.out.println("重复提交表单！");
        }

        Cookie[] cookies=request.getCookies();
        boolean login=false; //是否登录的标记，true:已经登录   false:没有登录
        String account=null; //登录账号
        String ssid=null;  //这是一个标记，通过cookie拿到的一个判断用户该不该成功登录的标记

        if(cookies !=null && cookies.length>0) {
            for(Cookie cookie:cookies) {
                if(cookie.getName().equals("userKey")) {
                    account=cookie.getValue();
                }
                if(cookie.getName().equals("ssid")) {
                    ssid=cookie.getValue();
                }
            }
        }
        if(account!=null && ssid!=null) {
            login=ssid.contentEquals(CookieUtils.md5Encrypt(username));
        }

        if(!login) {  //login:false  表示用户还没登录
            //用户第一次访问过来

            Criteria criteria = new Criteria();
            criteria.and("username").is(username);
            criteria.and("password").is(CookieUtils.md5Encrypt(password));
//            System.out.println(username);
//            System.out.println(CookieUtils.md5Encrypt(password));
            Query query = new Query(criteria);
            User user = mongoTemplate.findOne(query, User.class);


            String errorMsg="";

            if(user!=null){
                System.out.println(user);
                CookieUtils.createCookie(username,request,response,7*24*60*60); //选择类记住我一周
                request.getSession().setAttribute("user",user.getUsername());
//                moreUser(request,response);
                response.sendRedirect("/index");
            }else{
                System.out.println(user);
                request.setAttribute("errorMsg", "用户名或者密码错误");
//                response.sendRedirect("/login");
                request.getRequestDispatcher("/login").forward(request, response);
            }

        }else {
//            moreUser(request,response);
            request.getSession().setAttribute("user",username);
            User user = getMine(request,response);
            response.sendRedirect("/index");
        }
    }
    // 注销
    @RequestMapping(value = {"logout"})
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //把记录登录状态的cookie删除
        Cookie[] cookies=request.getCookies();
        if(cookies!=null && cookies.length>0) {
            for(Cookie cookie:cookies) {
                if(cookie.getName().equals("userKey")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                if(cookie.getName().equals("ssid")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        //把记录登录状态的session删除
        HttpSession session=request.getSession();
        if(session!=null) {
            session.removeAttribute("user");
        }
//        return "/login";
        response.sendRedirect("/index");
    }

    // 好友列表
    @RequestMapping(value = {"friendsList"})
    public String friendsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("user");
        if(username != null){
            myFriends(request,response);
            User user = getMine(request,response);
            request.setAttribute("avatar",user.getAvatar());
            return "/friendsList";
        }else{
            request.setAttribute("errorMsg", "请先登录！");
            return "/login";
        }

    }
    // 显示我的好友
//    @RequestMapping(value = {"myFriends"})
    public void myFriends(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//        String username = (String) request.getSession().getAttribute("user");
//        Query query = new Query(Criteria.where("username").is(username));
//        User user = mongoTemplate.findOne(query,User.class);
        User user = getMine(request,response);

        System.out.println("当前用户：");
        System.out.println(user);
        List<String> ids = user.getFriends();
        System.out.println("我的好友id列表：");
        System.out.println(ids);

        List<User> mylist = new ArrayList<User>();
        if(ids != null){
            for(String id : ids){
                Query query1 = new Query(Criteria.where("_id").is(id));
                User user1 = mongoTemplate.findOne(query1,User.class);
                mylist.add(user1);
            }
        }
        System.out.println("好友列表：");
        System.out.println(mylist);
        request.setAttribute("mylist",mylist);
//        request.getRequestDispatcher("/friendsList").forward(request, response);
    }

    // 添加好友
    @RequestMapping(value = {"addFriend"})
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        User user = getMine(request,response);
        List<String> ids = user.getFriends();

        System.out.println("添加id：");
        System.out.println(id);
        System.out.println(ids);
        System.out.println("----------");
        if( ids != null && ids.size() != 0 ){
            Boolean flag = true;

            for(String i : ids){
                System.out.println("i=============");
                System.out.println(i);
                if(i.equals(id)){
                    System.out.println("已添加过");
                    request.setAttribute("info", "已添加过");
                    flag = false;
                    break;
                }
            }
            if(flag){
                String username = (String) request.getSession().getAttribute("user");
                Query query = new Query(Criteria.where("username").is(username));
                Update update = new Update().push("friends", id);
                mongoTemplate.updateFirst(query, update, User.class);
            }
        }else{
            System.out.println("ids==null");
            System.out.println("为null 执行");
            String username = (String) request.getSession().getAttribute("user");
            Query query = new Query(Criteria.where("username").is(username));
            Update update = new Update().push("friends", id);
            mongoTemplate.updateFirst(query, update, User.class);
        }

        request.getRequestDispatcher("/moreFriends?pageIndex=1&pageSize=5").forward(request, response);

    }
    // 删除好友
    @RequestMapping(value = {"removeFriend"})
    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        System.out.println("删除id:");
        System.out.println(id);
        String username = (String) request.getSession().getAttribute("user");
        Query query = new Query(Criteria.where("username").is(username));

        Update update = new Update().pull("friends", id);

        mongoTemplate.updateFirst(query, update, User.class);

        request.getRequestDispatcher("/friendsList").forward(request, response);

    }

    // 所有用户
    @RequestMapping(value = {"moreFriends"})
    public String moreFriends(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("user");
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        request.setAttribute("pageIndex",pageIndex);

        if(username != null){
            User user = getMine(request,response);
            request.setAttribute("avatar",user.getAvatar());
            moreUser(request,response,pageIndex,pageSize);
            return "/moreFriends";
        }else{
            request.setAttribute("errorMsg", "请先登录！");
            return "/login";
        }

    }

//    @RequestMapping(value = {"moreUser"})
    public void moreUser(HttpServletRequest request, HttpServletResponse response,int pageIndex, int pageSize) throws ServletException, IOException {
        List<User> list = new ArrayList<User>();
//        list = mongoTemplate.findAll(User.class);

        Query query = new Query();
        query.skip((pageIndex-1)*pageSize);
        query.limit(pageSize);
        list = mongoTemplate.find(query,User.class);
        //查询总记录数
        int count = (int) mongoTemplate.count(query,User.class);
        float page = (float)count/pageSize;
        System.out.println(page);
        int pageNum = (int)Math.ceil(page);

        System.out.println("分页后得到的数据：");
        System.out.println(list);
        System.out.println("总记录数：");
        System.out.println(count);
        System.out.println("总页数：");
        System.out.println(pageNum);

        request.setAttribute("userList",list);
        request.setAttribute("pageNum",pageNum);
//        response.sendRedirect("/index");
    }


    // 存储用户头像
    @RequestMapping(value = {"saveAvatar"})
    public void savaAvatar(@RequestParam("muavatar") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            List<String> list = new ArrayList<String>();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    // 保存文件
                    list = saveFile(request, file, list);

                    String username = (String) request.getSession().getAttribute("user");
                    Query query = new Query(Criteria.where("username").is(username));
                    Update update = new Update().set("avatar", list.get(i));
                    mongoTemplate.upsert(query, update, User.class);
                }
            }
            //写着测试，删了就可以
            for (int i = 0; i < list.size(); i++) {
                System.out.println("集合里面的数据" + list.get(i));
            }

        request.getRequestDispatcher("/index").forward(request, response);

    }


    private List<String> saveFile(HttpServletRequest request, MultipartFile file, List<String> list) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 保存的文件路径(如果用的是Tomcat服务器，文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
                // )
                String filePath = request.getSession().getServletContext()
                        .getRealPath("/")
                        + "upload/" + file.getOriginalFilename();
                System.out.println(filePath);

                list.add(file.getOriginalFilename());
                File saveDir = new File(filePath);
                if (!saveDir.getParentFile().exists())
                    saveDir.getParentFile().mkdirs();

                // 转存文件
                file.transferTo(saveDir);
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // 修改座右铭
    @RequestMapping(value = {"updateMotto"})
    public void updateMotto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String motto = request.getParameter("motto");
        System.out.println(motto);
        String username = (String) request.getSession().getAttribute("user");
        Query query = new Query(Criteria.where("username").is(username));
        Update update = new Update().set("motto",motto);
        mongoTemplate.upsert(query, update, User.class);
        request.getRequestDispatcher("/index").forward(request, response);
    }

    //朋友圈动态
    @RequestMapping(value = {"info"})
    public String getInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("user");
        if(username != null){
            User user = getMine(request,response);
            request.setAttribute("avatar",user.getAvatar());
            getAllInfo(request, response);
            return "/info";
        }else{
            request.setAttribute("errorMsg", "请先登录！");
            return "/login";
        }

    }

    // 发布动态
    @RequestMapping(value = {"sendInfo"})
    public void sendInfo(@RequestParam("muavatar") MultipartFile[] files,
                         @RequestParam("text") String text,
                         HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = (String) request.getSession().getAttribute("user");
        Query query = new Query(Criteria.where("username").is(username));

        List<String> like = new ArrayList<String>();
        List<Comment> comments = new ArrayList<Comment>();

        List<String> list = new ArrayList<String>();
        Info info = new Info();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                // 保存文件
                list = saveFile(request, file, list);

                info = new Info(text,list);
            }
        }else{
            info = new Info(text);
        }


        Update update = new Update();
        update.addToSet("infos",info);
        mongoTemplate.upsert(query, update, "user");

        request.getRequestDispatcher("/info").forward(request, response);
    }


    // 显示朋友圈动态
    public void getAllInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        User user = getMine(request,response);

        List<String> ids = user.getFriends();
        System.out.println("我的好友id列表：");
        System.out.println(ids);

        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        if(ids != null){
            for(String id : ids){
                Query query1 = new Query(Criteria.where("_id").is(id));
                User user1 = mongoTemplate.findOne(query1,User.class);
                if(user1.getInfos() !=null){
                    for(Info i : user1.getInfos()){
                        UserInfo userInfo = new UserInfo(user1.getId(),user1.getNickname(),user1.getAvatar(),i);
                        userInfos.add(userInfo);
                    }
                }

            }
        }
        System.out.println("好友列表：");
        request.setAttribute("userInfos",userInfos);
        System.out.println(request.getAttribute("userInfos"));
//        request.getRequestDispatcher("/friendsList").forward(request, response);
    }

    // 点赞
    @RequestMapping(value = {"getlike"})
    public void getlike(@RequestParam("userId") ObjectId userId,@RequestParam("infoId") ObjectId infoId, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getMine(request,response);
        String userid = String.valueOf(user.getId());

        Userlike userlike = new Userlike();
        userlike.setUserId(userid);
        userlike.setUserAvatar(user.getAvatar());


        Query query = Query.query(Criteria.where("_id").is(userId).and("infos._id").is(infoId));
//        Update update = new Update().addToSet("infos.$.like",user.getAvatar()).addToSet("infos.$.likeId",user.getId());
        Update update = new Update().addToSet("infos.$.like",userlike);

        mongoTemplate.upsert(query, update, User.class);

        System.out.println("点赞。。。");
        request.getRequestDispatcher("/info").forward(request, response);

    }

    // 发布评论
    @RequestMapping(value = {"sendComment"})
    public void sendComment(@RequestParam("userId") ObjectId userId,@RequestParam("infoId") ObjectId infoId,@RequestParam("text") String text, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getMine(request,response);
        String userid = String.valueOf(user.getId());

        Comment comment = new Comment();
        comment.setUserId(userid);
        comment.setContent(text);
        comment.setNickname(user.getNickname());

        Query query = Query.query(Criteria.where("_id").is(userId).and("infos._id").is(infoId));
        Update update = new Update().addToSet("infos.$.comment",comment);
        mongoTemplate.upsert(query, update, User.class);

        System.out.println("评论。。。");
        request.getRequestDispatcher("/info").forward(request, response);

    }


}















