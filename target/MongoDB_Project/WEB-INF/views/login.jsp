<%@page import="java.util.UUID"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>登录页</title>
    <!-- Bootstrap core CSS -->
    <link href="<%=basePath%>/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<style type="text/css">
    body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #eee;
    }

    .form-signin {
        max-width: 330px;
        padding: 15px;
        margin: 0 auto;
    }
    .form-signin .form-signin-heading,
    .form-signin .checkbox {
        margin-bottom: 10px;
    }
    .form-signin .checkbox {
        font-weight: normal;
    }
    .form-signin .form-control {
        position: relative;
        height: auto;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        padding: 10px;
        font-size: 16px;
    }
    .form-signin .form-control:focus {
        z-index: 2;
    }
    .form-signin input[type="email"] {
        margin-bottom: -1px;
        border-bottom-right-radius: 0;
        border-bottom-left-radius: 0;
    }
    .form-signin input[type="password"] {
        margin-bottom: 10px;
        border-top-left-radius: 0;
        border-top-right-radius: 0;
    }
</style>
<script type="text/javascript">
    window.onload=function(){
        var btn=document.getElementById("submitbtn");
        btn.onclick=function(){
            this.disabled=true;//让按钮失效，不可用
            this.parentNode.submit();//提交
        }
    }
</script>

<body>
<%
    String uuid=UUID.randomUUID().toString();
    session.setAttribute("uuid",uuid);
%>
<div class="container">

    <c:if test="${not empty requestScope.errorMsg}">
        <p style="color:red;font-weight:bolder; text-align: center;">${errorMsg}</p>
    </c:if>

    <form class="form-signin" id="login" method="post" action="userLogin">
        <input type="hidden" name="token" value="<%=uuid%>">
        <%--@declare id="inputusername"--%><%--@declare id="inputpassword"--%><h2 class="form-signin-heading">登录</h2>
        <label for="inputUsername" class="sr-only">Username</label>
        <input type="username" name="username" class="form-control" placeholder="Username" required>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" name="password" class="form-control" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit" id="submitbtn">登录</button>
    </form>
    <p style="text-align: center;">
        <a href="/register">先去注册！</a>
    </p>
</div> <!-- /container -->
</body>
</html>

