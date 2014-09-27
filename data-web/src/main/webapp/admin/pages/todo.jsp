<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

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
<!--[if !(IE 6) | !(IE 7) | !(IE 8) | !(IE 9)  ]><!-->
<html lang="zh-CN">
<!--<![endif]-->

<shiro:notAuthenticated>
	<script type="text/javascript">
    	window.location.href = "<%=basePath%>login.jsp";
        window.parent.location.href = "<%=basePath%>login.jsp";
    </script>
</shiro:notAuthenticated>

<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>todo list</title>
<link type="text/css" rel="stylesheet" href="admin/assets/css/easyui.css" />
<link rel="stylesheet" type="text/css" href="admin/assets/css/base.css" />
<link rel="stylesheet" type="text/css" href="admin/assets/css/style.css" />
<script src="admin/assets/js/common.js" type="text/javascript"></script>

<link rel="stylesheet" type="text/css"	href="admin/assets/css/datePicker.css" />
<link rel="stylesheet" type="text/css"	href="admin/assets/css/textbox.css" />
<link rel="stylesheet" type="text/css"	href="admin/assets/css/window.css" />
<link rel="stylesheet" type="text/css"	href="admin/assets/css/datagrid.css" />

<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

body {
	font: 12px/1.5 Arial;
	color: #666;
	background: #fff;
}

ul,li {
	list-style: none;
}

.liout{
 background-color:#f1f1f1;
 border:1px solid #f1f1f1;
}
li span

img {
	border: 0 none;
}

.combo-arrow{
background:url('../images/combo-arrow.png') no-repeat center center
}

</style>
<script type="text/javascript" src="admin/assets/js/libs/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="admin/assets/js/libs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="admin/assets/js/libs/jquery.date_input.pack.js"></script>
<script type="text/javascript" src="admin/assets/js/libs/jquery.textbox.js"></script>
<script type="text/javascript" src="admin/assets/js/jquery.scrollTo.js"></script>

<!--script type="text/javascript" src="admin/assets/js/libs/jquery.datagrid.js"></script-->

</head>
<body>
	<div><h1 align="center" style="font-size:25px">敬请期待</h1></div>

</body>
</html>


