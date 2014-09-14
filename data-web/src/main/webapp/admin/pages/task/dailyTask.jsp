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
	<title>保有任务管理</title>
	<link type="text/css" rel="stylesheet" href="admin/assets/css/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="admin/assets/css/base.css" />
	<link rel="stylesheet" type="text/css" href="admin/assets/css/style.css" />
	<script src="admin/assets/js/common.js" type="text/javascript"></script>
	
	<link rel="stylesheet" type="text/css" href="admin/assets/css/datePicker.css"/>
	<link rel="stylesheet" type="text/css" href="admin/assets/css/textbox.css"/>
	<style type="text/css"> 
		*{ margin:0; padding:0;}
		body { font:12px/1.5 Arial; color:#666; background:#fff;}
		ul,li{ list-style:none;}
		img{border:0 none;}
	</style> 
	<script type="text/javascript" src="admin/assets/js/libs/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="admin/assets/js/libs/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="admin/assets/js/libs/jquery.date_input.pack.js"></script>
    <script type="text/javascript" src="admin/assets/js/libs/jquery.textbox.js"></script>
    
	<script type="text/javascript">
		//删除一个数据
		function optDelete(id) {
			if(Pony.checkedCount("ck") <= 0) {
				alert("请选择您要操作的数据!");
				return;
			}
			if(!confirm("您确定删除吗？")) {
				return;
			}
			window.location = "<%=basePath%>member/o_delete.do?id="+id;
		}
		
	</script>
</head>
<body>
<script type="text/javascript">
	$(function(){
		$('#datePicker').date_input();
		d = new Date();
		month = d.getMonth() + 1;
		if(month < 10)
			month = "0" + month;
		date = d.getDate();
		if(date < 10)
			date = "0" + date;
		dateString = d.getFullYear()+"-"+ month + "-" + date; 
		$('#datePicker').val(dateString);
		$('#currentMonth').html(d.getFullYear()+"年"+ month +"月");
		
		$('#datePicker').bind('change', function(e){
			yearMonth = $('#datePicker').val().split('-');
			$('#currentMonth').html(yearMonth[0] + "年" + yearMonth[1] + "月");
		});
	});
</script>
	<div class="container">
		<div class="toolbar">
			<div class="crumbs">
				<span>任务管理</span> -&gt; <span>保有任务</span> 
			</div>
			<div style="padding:5px">
				<div style="float:left;padding:5px">
					<input class="textbox" style="border-radius:3px;width:180px;font-size:14px;padding:7px" placeholder="筛选手机号">&nbsp;&nbsp;
				</div>
				<div class="action" style="float:left;">
					<a href="member/v_add.do" class="btn" target="_self">过滤</a>
				</div>
				<div style="text-align:center;float:left;width:50%;padding:7px">
					<b><span id="currentMonth" style="font-size:20px;"></span></b>
				</div>
				<div style="float:right; width:250px; padding:5px">
					<input type="text" id="datePicker" class="date_picker" placeholder="点击选择日期" readOnly=true/>	
				</div>
			</div>
		</div>
		<div class="mod">
			<div class="bd">
    		<table class="ui-table">
    			<thead>
    				<tr>
						<th width="30">
							<input type="checkbox" name="ck_all" id="ck_all" onclick="Pony.checkboxSlt('ck',this.checked);"/>
						</th>
						<!--<th>ID</th>
						<th>用户名</th>
						<th>电子邮箱</th>
						<th>会员组</th>
						<th>最后登录</th>
						<th>登录</th>
						<th>禁用</th>
						<th width="140">操作选项</th>-->
						<th>手机号</th>
						<th>合约开始时间</th>
						<th>合约到期时间</th>
						<th>合约剩余</th>
						<th>当前套餐</th>
						<th>任务状态</th>
						<th>操作</th>
    				</tr>
    			</thead>
    			<tbody>
    			<s:iterator value="bills" status="status">
	    			<s:if test="#status.even">
	    				<tr class="even">
	    			</s:if>
	    			<s:else>
	    				<tr>
	    			</s:else>
    					<tr>
    							<td class="tc"><input type='checkbox' name='ck' value='${flag.id}' /></td>
								<td><s:property value="phoneNo"/></td>
								<td><s:property value="contractFrom.longValue()"/></td>
								<td><s:property value="contractTo.longValue()"/></td>
								<td>合约剩余</td>
								<td><s:property value="package"/></td>
								
					<s:if test="#status.even">
	    				<td color="green">待处理</td>
	    			</s:if>
	    			<s:else>
	    				<td color="red">已处理</td>
	    			</s:else>
								<td>
									<a class="btn" href="member/v_update.do?id=${flag.id}">查看</a>
									<!--a href="javascript:;" class="btn" onclick="optDelete(${flag.id});">删除</a-->
								</td>
	    					</tr>
    			</s:iterator>
    			</tbody>
    		</table>
			</div>
			<!-- /.mod-bd -->
			<div class="ft">
		    	<pg:pager items="${userPager.total}" maxPageItems="10" maxIndexPages="10" url="member/v_list.do" export="currentPageNo = pageNumber">
		    		<pg:index export="totalItems = itemCount">
		    		<div class="pager">
					    <pg:page export="firstItem, lastItem">
				        <span class="info">共${totalItems}条/${firstItem}-${lastItem}页</span>
				        </pg:page>
				       	<pg:first> 
				       	<a class="start" href="${pageUrl}">首页</a>
				       	</pg:first>
				       	<pg:prev>
				        <a class="prev" href="${pageUrl}">上一页</a>
				        </pg:prev>
				        <pg:pages>
				        <c:choose>
					        <c:when test="${currentPageNo eq pageNumber}">
					        	<span class="page current">${pageNumber}</span>
					        </c:when>
					        <c:otherwise>
					        <a class="page" href="${pageUrl}">${pageNumber}</a>
					        </c:otherwise>
				        </c:choose>
				        </pg:pages>
				        <pg:next>
				        <a class="next" href="${pageUrl}">下一页</a>
				        </pg:next>
				        <pg:last>
				        <a class="end" href="${pageUrl}">末页</a>
				        </pg:last>
			    	</div><!-- /.pager -->
			    	</pg:index>
			    </pg:pager>
			</div>
			<!-- /.mod-ft -->
		</div>
		<!-- /.mod -->
	</div>
	<!-- /.container  -->
	
</body>
</html>
