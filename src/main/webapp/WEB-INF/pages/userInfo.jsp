<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    request.setCharacterEncoding("utf-8");
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>微信用户信息</title>
</head>

<body>
<p>
    头像：<img src="${headimgurl}" width="60" height="60">
</p>
<p>
    昵称：${nickname}
</p>
<p>
    openid：${openid}
</p>
<p>
    性别：<c:if test="${sex == 1}">男</c:if> <c:if test="${sex == 2}">女</c:if>
</p>
<p>
    地区：${country} &nbsp; ${province} &nbsp; ${city}
</p>
</body>

</html>