
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <meta name="description" content="">
    <meta name="author" content="">
    <title>注册页</title>
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
    .form-signin input{
        margin-bottom: 10px;
        border-top-left-radius: 0;
        border-top-right-radius: 0;
    }


</style>


<body>
<div class="container">
    <form class="form-signin" id="register" method="post" action="saveUser">
        <%--@declare id="inputusername"--%><%--@declare id="inputpassword"--%><%--@declare id="inputnickname"--%><h2 class="form-signin-heading">注册</h2>
        <label for="inputUsername" class="sr-only">Username</label>
        <input type="text" name="username" class="form-control" placeholder="Username" required autofocus>

        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" name="password" class="form-control" placeholder="Password" required>

        <label for="inputNickname" class="sr-only">Nickname</label>
        <input type="text" name="nickname" class="form-control" placeholder="Nickname" required >

        <button class="btn btn-lg btn-primary btn-block" type="submit">注册</button>
    </form>
</div> <!-- /container -->
</body>
</html>
