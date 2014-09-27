<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
<%@ taglib uri ="/struts-tags" prefix ="s" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%><!DOCTYPE html>
<!--[if IE 6]><html class="ie6 lte9 lte8 lte7" lang="zh-CN"><![endif]-->
<!--[if IE 8]><html class="ie8 lte9 lte8" lang="zh-CN"><![endif]-->
<!--[if IE 9]><html class="ie9 lte9" lang="zh-CN"><![endif]-->
<!--[if IE 7]><html class="ie7 lte9 lte8 lte7" lang="zh-CN"><![endif]-->
<!--[if !(IE 6) | !(IE 7) | !(IE 8) | !(IE 9)  ]><!--><html lang="zh-CN"><!--<![endif]-->

<shiro:notAuthenticated>
    <script type="text/javascript">
    	window.location.href = "<%=basePath%>login.jsp";
        window.parent.location.href = "<%=basePath%>login.jsp";
    </script>
</shiro:notAuthenticated>

<head>
	<base href="<%=basePath%>" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>修改密码</title>
	<link rel="stylesheet" type="text/css" href="admin/assets/css/base.css" />
	<link rel="stylesheet" type="text/css" href="admin/assets/css/style.css" />
	<link rel="stylesheet" type="text/css"	href="admin/assets/css/textbox.css" />
</head>
<body>
	<div class="container" align="center">
			<div style="height:80px"></div>
			<div align=center style="padding: 5px;font-size: 14px; ">
				<span>请输入老密码：</span>
				<input type="password" class="textbox" id="oldpasswd" style="border-radius: 3px; width: 150px; padding: 7px " placeholder="请输入老密码">&nbsp;&nbsp;
			</div>
			<div style="height:20px"></div>
			<div align=center style="padding: 5px;font-size: 14px; ">
				<span style="vertical-align:bottom">请输入新密码：</span>
				<input type="password" class="textbox" id="newpasswd1" style="border-radius: 3px; width: 150px; padding: 7px " placeholder="请输入新密码">&nbsp;&nbsp;
			</div>
			<div style="height:5px"></div>
			<div align=center style="padding: 5px;font-size: 14px; ">
				<span style="vertical-align:bottom">请确认新密码：</span>
				<input type="password" class="textbox" id="newpasswd2" style="border-radius: 3px; width: 150px; padding: 7px " placeholder="再次输入新密码">&nbsp;&nbsp;
			</div>
			<div style="height:30px"><span id="msg" style="color:red"></span></div>
			
			<div>
				<span style="width:200px"></span><a class="btn" id="ok">确定</a>
			</div>
	</div>
	
</body>
<script type="text/javascript" src="admin/assets/js/libs/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#ok').bind('click', function(e){
			passwd = $('#newpasswd2').val();
			oldpasswd = $('#oldpasswd').val();
			if($('#newpasswd1').val() != $('#newpasswd2').val()) 
				$('#msg').html('两次输入的新密码不一样');
			else {
				$.ajax({
					  type: "POST",
					  url: "setPasswd",
					  data: { "password": passwd, "oldpassword": oldpasswd},
					  success:function(data){
					      $('#msg').html(data['msgText']);
					  }
				});
			}
		});
		
		$('#newpasswd1').bind('change', function(e){
			if($('#newpasswd1').val() != $('#newpasswd2').val()) 
				$('#msg').html('两次输入的新密码不一样');
			else
				$('#msg').html('');
		});
		
		$('#newpasswd2').bind('change', function(e){
			if($('#newpasswd1').val() != $('#newpasswd2').val()) 
				$('#msg').html('两次输入的新密码不一样');
			else
				$('#msg').html('');
		});
		
	});
</script>
</html>
