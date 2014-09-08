<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主页</title>
	<shiro:notAuthenticated>
    <script type="text/javascript">
        window.location.href = 'login.jsp';
    </script>
    </shiro:notAuthenticated>
</head>

<body>
	<a href="login_input">去登陆</a>

	<form action="freemarkerAction.action">
		姓名:<input type="text" name="name" /><br />
		 年龄:<input type="text" name="age" /><br />
		 <input type="submit" value="提交" />
	</form>
</body>
</html>