<%@page import="java.util.UUID"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>动态</title>
    <script src="<%=basePath%>/js/jquery-3.3.1.min.js"></script>
    <link href="<%=basePath%>/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="<%=basePath%>/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <style>
        .tab-content{
            margin-top: 30px;
        }
        #myinfo{
            display: inline-block;
            width: 100%;
        }
        #localImag{

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
            min-height: 150px;
            border-bottom: 1px solid #31b0d5;
        }
    </style>
</head>
<body>
    <div class="container">
        <ul class="nav nav-tabs">
            <li role="presentation" ><a href="/index" >个人中心</a></li>
            <li role="presentation"><a href="/friendsList">好友列表</a></li>
            <li role="presentation" class="active"><a href="#" >朋友圈</a></li>
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
            <a  href="#">发布动态</a>
            <div id="myinfo">
                <form action="/sendInfo" method="post" enctype="multipart/form-data">
                    <input type="text" placeholder="这一刻的想法。。。" name="text">
                    <input type='button' value="上传图片"  OnClick='javascript:$("#doc").click();'/>
                    <input type="file" name="muavatar"  id="doc" onchange="showImage();" style="display: none;" />

                    <input type="submit" value="发送"/>
                </form>
                <div id="localImag">
                    <img id="preview" width=-1 height=-1 style="diplay:none" />
                </div>
            </div>


            <ul id="infos">
                <c:if test="${not empty userInfos}">
                    <c:forEach items="${userInfos}" var="userinfo" varStatus="idxStatus">
                        <li>
                            <div class="infoAvatar">
                                <img style="width: 50px;height: 50px;" src="<%=basePath%>/upload/${userinfo.avatar}" alt="">
                                <span style="height: 50px;line-height: 50px;margin-left: 10px;">${userinfo.nickname}</span>
                            </div>
                            <div class="infoContent">
                                <p>${userinfo.info.text }</p>
                                <p>
                                    <img style="width: 80px;height: 80px;" src="<%=basePath%>/upload/${userinfo.info.images[0]}" alt="">
                                </p>
                                <p>
                                        ${userinfo.info.createDate}
                                        <a href="/getlike?userId=${userinfo._id}&infoId=${userinfo.info._id}">点赞</a>
                                </p>
                                <P style="margin-top: 20px; width: 100%; display: inline-block;">
                                    <c:if test="${not empty userinfo.info.like}">
                                        <c:forEach items="${userinfo.info.like}" var="like" varStatus="idxStatus">
                                            <img src="<%=basePath%>/upload/${like.userAvatar}" style="width: 30px;height: 30px; float:left;" alt="">
                                        </c:forEach>
                                    </c:if>
                                </P>

                                <form action="sendComment?userId=${userinfo._id}&infoId=${userinfo.info._id}" method="post">
                                    <input type="text" name="text">
                                    <input type="submit" value="发送">
                                </form>

                                <div class="comment">
                                    <c:if test="${not empty userinfo.info.comment}">
                                        <c:forEach items="${userinfo.info.comment}" var="comment" varStatus="idxStatus">
                                            <p>
                                                <span style="color: #2e6da4;font-size: 18px;margin-right: 10px;">${comment.nickname}:</span>
                                                ${comment.content}
                                            </p>

                                        </c:forEach>
                                    </c:if>
                                </div>

                            </div>

                        </li>
                    </c:forEach>
                </c:if>
            </ul>

        </div>
    </div>

    <script type="text/javascript">
        function showImage() {
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
