package com.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.entity.User;
import com.mongodb.util.CookieUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2019/11/15.
 */
@Controller
public class UserController {
    @Autowired
    MongoTemplate mongoTemplate;

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
            moreUser(request,response);
            request.getSession().setAttribute("user",username);
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
            System.out.println("ids!=null / 0");
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

        request.getRequestDispatcher("/moreFriends").forward(request, response);

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
        moreUser(request,response);
        return "/moreFriends";
    }

//    @RequestMapping(value = {"moreUser"})
    public void moreUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> list = new ArrayList<User>();
        list = mongoTemplate.findAll(User.class);
        request.setAttribute("userList",list);
//        response.sendRedirect("/index");
    }


}
