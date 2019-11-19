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
</head>
<body>
    <div class="container">
        <ul class="nav nav-tabs">
            <li role="presentation" class="active"><a href="#home" data-toggle="tab">个人中心</a></li>
            <li role="presentation"><a href="#friends" data-toggle="tab">好友列表</a></li>
            <li role="presentation"><a href="#infos" data-toggle="tab">朋友圈</a></li>
            <li role="presentation"><a href="#more_friends" data-toggle="tab">更多好友</a></li>
            <li class="navbar-text navbar-right">
                <c:choose>
                    <c:when test="${empty sessionScope.user }"><a href="/login">未登录</a></c:when>
                    <c:otherwise>欢迎！${sessionScope.user}<a href="/logout"> 注销</a></c:otherwise>
                </c:choose>
            </li>
        </ul>
        <div id="container" class="tab-content">
            <div class="tab-pane active" id="home">个人中心</div>
            <div class="tab-pane fade" id="friends">好友列表</div>
            <div class="tab-pane fade" id="infos">朋友圈</div>
            <div class="tab-pane fade" id="more_friends">朋友圈</div>
        </div>
    </div>

</body>
</html>