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
            height: 450px;
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
        .pagination ul{
            /*list-style: noen;*/
        }
        .pagination ul li{
            float: left;
            width: 30px;
            height: 30px;
            line-height: 30px;
            border: 1px solid #5bc0de;
            text-align: center;
            margin: 0 3px;
            list-style: none;
        }
        .pagination ul li a{
            display: inline-block;
            width: 100%;
            height: 100%;
        }
        .pagination ul li a:hover,
        .pagination ul li a:active{

            background: #46b8da;
            color: #fff;
        }
        .pagination ul li a strong{
            display: inline-block;
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body>
<div class="container">
    <ul class="nav nav-tabs">
        <li role="presentation" ><a href="/index" >个人中心</a></li>
        <li role="presentation"><a href="/friendsList">好友列表</a></li>
        <li role="presentation"><a href="/info" >朋友圈</a></li>
        <li role="presentation" class="active"><a href="/moreFriends?pageIndex=1&pageSize=5" >更多好友</a></li>
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
        <c:if test="${not empty requestScope.info}">
            <p style="color:red;font-weight:bolder; text-align: center;">${info}</p>
        </c:if>

        <ul class="users">
            <c:if test="${not empty userList}">
                <c:forEach items="${userList}" var="user">
                    <li class="user">
                        <c:choose>
                            <c:when test="${empty user.avatar }">
                                <img class="avatar"  src="<%=basePath%>/imgs/favicon.jpg" alt="">
                            </c:when>
                            <c:otherwise>
                                <img class="avatar" src="<%=basePath%>/upload/${user.avatar}" alt="">
                            </c:otherwise>
                        </c:choose>
                        <span style="display: none;">${user.id }</span>
                        <span style="display: none;">${user.username }</span>
                        <span style="display: none;">${user.password }</span>
                        <span>${user.nickname }</span>
                        <span>${user.motto}</span>
                        <spna>
                            <c:choose>
                                <c:when test="${user.username == sessionScope.user}">
                                    <a href="/index"></a>
                                </c:when>
                                <c:otherwise><a href="/addFriend?id=${user.id}">添加</a></c:otherwise>
                            </c:choose>
                        </spna>
                    </li>
                </c:forEach>
            </c:if>
        </ul>

        <div class="pagination">
            <ul>
                <c:if test="${not empty pageNum}">
                    <c:forEach var="page" begin="1" end="${pageNum}">
                        <li>
                            <a href="/moreFriends?pageIndex=${page}&pageSize=5">
                                <c:choose>
                                    <c:when test="${pageIndex == page}">
                                        <strong style="background: #46b8da;color: #fff;">  ${page}</strong>
                                    </c:when>
                                    <c:otherwise><strong>${page}</strong></c:otherwise>
                                </c:choose>

                            </a>
                        </li>
                    </c:forEach>
                </c:if>
            </ul>
        </div>


    </div>
</div>

<script>

</script>
</body>
</html>