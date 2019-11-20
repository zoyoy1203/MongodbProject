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
        <li role="presentation" ><a href="/index" >个人中心</a></li>
        <li role="presentation"><a href="/friendsList">好友列表</a></li>
        <li role="presentation"><a href="#" >朋友圈</a></li>
        <li role="presentation" class="active"><a href="/moreFriends" >更多好友</a></li>
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

        <table  style="margin:100px;border:1px solid black;" border="1" cellpadding="0" cellspacing="0">
            <tr>
                <td>用户id</td>
                <td>用户名称</td>
                <td>用户密码</td>
                <td>用户昵称</td>
                <td>操作</td>
            </tr>

            <c:if test="${not empty userList}">
                <c:forEach items="${userList}" var="user">
                    <tr>
                        <td>${user.id }</td>
                        <td>${user.username }</td>
                        <td>${user.password }</td>
                        <td>${user.nickname }</td>
                        <td>
                            <%--<form action="/addFriend"class="registSubmit">--%>
                                <%--<input name="id" value="${user.id}">--%>
                                <%--<input type="submit" >添加</input>--%>
                            <%--</form>--%>
                            <c:choose>
                                <c:when test="${user.username == sessionScope.user}">
                                    <a href="/index"></a>
                                </c:when>
                                <c:otherwise><a href="/addFriend?id=${user.id}">添加</a></c:otherwise>
                            </c:choose>

                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </table>

    </div>
</div>

<script>

</script>
</body>
</html>