<%@page import="java.util.UUID"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String url = request.getRequestURI();
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>个人中心</title>
    <script src="<%=basePath%>/js/jquery-3.3.1.min.js"></script>
    <link href="<%=basePath%>/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="<%=basePath%>/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <style>
        .tab-content{
            margin-top: 30px;
        }
        .avatar{
            position: relative;
            width: 30%;
            height: auto;
            float: left;
        }
        #localImag{
            position: absolute;
            top: 0;
            left: 120px;
        }
        .updateAvatar{
            margin-top:20px;
        }
        .info{
            float: left;
            width: 70%;
        }
        .info_ul{
            list-style: none;
            border-left: 2px solid #28a4c9;
        }
        .info_ul li{
            height: 50px;
        }
        #infos{
            display: inline-block;
            width: 100%;
            height: auto;
            margin-left: 30%;
        }
        #infos li{
            display: inline-block;
            margin-top: 40px;
            width: 100%;

        }
        .infoAvatar{
            width: 120px;
            float: left;
        }
        .infoContent{
            float: left;
            width: 380px;
            min-height: 200px;
            border: 1px solid #31b0d5;
        }
        
    </style>
</head>
<body>
    <div class="container">
        <ul class="nav nav-tabs">
            <li role="presentation" class="active"><a href="/index" >个人中心</a></li>
            <li role="presentation"><a href="/friendsList">好友列表</a></li>
            <li role="presentation"><a href="/info" >朋友圈</a></li>
            <li role="presentation" ><a href="/moreFriends" >更多好友</a></li>
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
        <div id="container" class="tab-content">
            <div class="avatar">
                <c:choose>
                    <c:when test="${empty requestScope.avatar }">
                        <img style="width: 100px;height: 100px;" src="<%=basePath%>/imgs/favicon.jpg" alt="">
                    </c:when>
                    <c:otherwise>
                        <img style="width: 100px;height: 100px;" src="<%=basePath%>/upload/${avatar}" alt="">
                    </c:otherwise>
                </c:choose>
                <div id="localImag">
                    <img id="preview" width=-1 height=-1 style="diplay:none" />
                </div>
                <form class="updateAvatar" action="saveAvatar" method="post" enctype="multipart/form-data">
                    <input type='button' value="上传头像"  OnClick='javascript:$("#doc").click();'/>

                    <input type="file" name="muavatar"  id="doc" onchange="showImage();" style="display: none;" />
                    <input type="submit" id="submit" value="修改头像"style="display: none;"/>
                </form>
            </div>
            <div class="info">
                <ul class="info_ul">
                    <li>您的ID:  <span>${myInfo.id }</span></li>
                    <li>用户名:  <span>${myInfo.username }</span></li>
                    <li>昵称:  <span>${myInfo.nickname }</span></li>
                    <li>
                        座右铭：
                        <span>
                            <form action="/updateMotto" method="post">
                                 <input type="text" value="${myInfo.motto}" name="motto">
                                <button type="submit">修改座右铭</button>
                            </form>
                        </span>
                    </li>
                </ul>
            </div>
        </div>

        <ul id="infos">
            <c:if test="${not empty myInfo.infos}">
                <c:forEach items="${myInfo.infos}" var="info">
                    <li>
                        <div class="infoAvatar">
                            <c:choose>
                                <c:when test="${empty requestScope.avatar }">
                                    <img style="width: 50px;height: 50px;display: block;" src="<%=basePath%>/imgs/favicon.jpg" alt="">
                                </c:when>
                                <c:otherwise>
                                    <img style="width: 50px;height: 50px;display: block;" src="<%=basePath%>/upload/${avatar}" alt="">
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="infoContent">
                            <p>${info.text }</p>
                            <p>
                                <img style="width: 80px;height: 80px;" src="<%=basePath%>/upload/${info.images[0]}" alt="">
                            </p>
                        </div>
                        
                       

                    </li>
                </c:forEach>
            </c:if>
        </ul>
    </div>

    <script type="text/javascript">
        function showImage() {
            var submit = document.getElementById("submit");
            submit.style.display="inline-block";
            var docObj = document.getElementById("doc");
            var imgObjPreview = document.getElementById("preview");
            if (docObj.files && docObj.files[0]) {
                //火狐下，直接设img属性
                imgObjPreview.style.display = 'block';
                imgObjPreview.style.width = '100px';
                imgObjPreview.style.height = '100px';
                //imgObjPreview.src = docObj.files[0].getAsDataURL();
                //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
                imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
            } else {
                //IE下，使用滤镜
                docObj.select();
                var imgSrc = document.selection.createRange().text;
                var localImagId = document.getElementById("localImag");
                //必须设置初始大小
                localImagId.style.width = "250px";
                localImagId.style.height = "200px";
                //图片异常的捕捉，防止用户修改后缀来伪造图片
                try {
                    localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
                    localImagId.filters
                        .item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
                } catch (e) {
                    alert("您上传的图片格式不正确，请重新选择!");
                    return false;
                }
                imgObjPreview.style.display = 'none';
                document.selection.empty();
            }
            return true;
        }
    </script>
</body>
</html>