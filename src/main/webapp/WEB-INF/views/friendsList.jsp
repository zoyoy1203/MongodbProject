<%@page import="java.util.UUID"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>个人中心</title>
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <link href="<%=basePath%>/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="<%=basePath%>/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <style>
        .users{
            list-style: none;
        }
        .user{
            display: inline-block;
            width: 100%;
            height: 70px;
            line-height: 70px;
            margin-top: 20px;
        }
        .avatar{
            width: 50px;
            height: 50px;
        }
        .user span{
            display: inline-block;
            min-width: 100px;
            margin-left: 30px;
        }
    </style>
</head>
<body>
<div class="container">
    <ul class="nav nav-tabs">
        <li role="presentation"><a href="/index" >个人中心</a></li>
        <li role="presentation" class="active"><a href="">好友列表</a></li>
        <li role="presentation"><a href="#" >朋友圈</a></li>
        <li role="presentation"><a href="/moreFriends" >更多好友</a></li>
        <li role="presentation" class="navbar-text navbar-right">
            <c:choose>
                <c:when test="${empty requestScope.avatar }">
                    <img style="width: 50px;height: 50px;" src="<%=basePath%>/imgs/favicon.jpg" alt="">
                </c:when>
                <c:otherwise>
                    <img style="width: 50px;height: 50px;" src="<%=basePath%>/upload/${avatar}" alt="">
                </c:otherwise>
            </c:choose>
        </li>
        <li role="presentation" class="navbar-text navbar-right">
            <c:choose>
                <c:when test="${empty sessionScope.user }">
                    <a href="/login">未登录</a>
                    <a href="/register">注册</a>
                </c:when>
                <c:otherwise>欢迎！${sessionScope.user}<a href="logout"> 注销</a></c:otherwise>
            </c:choose>
        </li>
    </ul>
    <div id="container">

        <ul class="users">
            <c:if test="${not emptymylist}">
                <c:forEach items="${mylist}" var="user">
                    <li class="user">
                        <c:choose>
                            <c:when test="${empty user.avatar }">
                                <img class="avatar"  src="<%=basePath%>/imgs/favicon.jpg" alt="">
                            </c:when>
                            <c:otherwise>
                                <img class="avatar" src="<%=basePath%>/upload/${user.avatar}" alt="">
                            </c:otherwise>
                        </c:choose>
                        <span style="display: none;">${user.username }</span>
                        <span style="display: none;">${user.password }</span>
                        <span>${user.nickname }</span>
                        <span>${user.motto}</span>
                        <span><a href="/removeFriend?id=${user.id}">删除好友</a></span>

                    </li>
                </c:forEach>
            </c:if>
        </ul>



    </div>
</div>

<script>

</script>
</body>
</html>