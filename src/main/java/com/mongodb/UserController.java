package com.mongodb;

import com.mongodb.entity.User;
import com.mongodb.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import java.io.UnsupportedEncodingException;


/**
 * Created by Administrator on 2019/11/15.
 */
@Controller
public class UserController {
    @Autowired
    MongoTemplate mongoTemplate;

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
    public String userLogin(Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
            System.out.println(username);
            System.out.println(CookieUtils.md5Encrypt(password));
            Query query = new Query(criteria);
            User user = mongoTemplate.findOne(query, User.class);


            String errorMsg="";

            if(user!=null){
                System.out.println(user);
                CookieUtils.createCookie(username,request,response,7*24*60*60); //选择类记住我一周
                request.getSession().setAttribute("user",user.getUsername());
                return "/index";
            }else{
                System.out.println(user);
                request.setAttribute("errorMsg", "用户名或者密码错误");
                System.out.println("2222");
                String err = (String) request.getAttribute("errorMsg");
                System.out.println(err);
                return "/login";
            }

        }else {
            request.getSession().setAttribute("user",username);
            return "/index";
        }
    }
    // 注销
    @RequestMapping(value = {"logout"})
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        return "/login";
    }

    // 显示所有用户
//    @RequestMapping(value = {"moreUser"})
//    public String moreUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }

//    @RequestMapping(value = {"addFriend"})
//    public String addFriend(Model model, @RequestParam id){
//        User user = new User();
//        user.setNickname(nickname);
//        user.setPassword(MD5Util.encrypt(password));
//        user.setUsername(username);
//        mongoTemplate.save(user);
//        return "/login";
//    }
}
