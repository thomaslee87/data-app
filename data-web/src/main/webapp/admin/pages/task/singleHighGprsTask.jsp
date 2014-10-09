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
<title>保有任务管理</title>
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
	<div class="container">
		<div class="toolbar">
			<div class="crumbs">
				<span>任务管理</span> -&gt; <span>保有任务</span>
			</div>
			<div style="padding: 5px">
				<div style="float: left; padding: 5px">
					<input class="textbox" id="phone"
						style="border-radius: 3px; width: 150px; font-size: 14px; padding: 7px"
						placeholder="筛选手机号">&nbsp;&nbsp;
				</div>
				<div class="action" style="float: left;">
					<a class="btn" id="filt">过滤</a>
				</div>
				
				<div
					style="text-align: center; float: left; width: 50%; padding: 7px">
					<b><span id="currentMonth" style="font-size: 20px;"><s:property
								value="theMonthString" />
							<s:if test="null==bills||bills.isEmpty()"> 暂无数据</s:if></span></b>
				</div>
				<div style="float: right; width: 250px; padding: 5px">
					<input type="text" id="datePicker" class="date_picker"
						placeholder="点击选择日期" readOnly=true
						value=<s:property value="calenderString"/> />
				</div>
			</div>
		</div>
		<div class="mod">
			<div class="bd">
				<table id="consumer_grid" class="ui-table">
					<thead>
						<tr>
							<th width="100px" style="display:none">
								<select id="ordertype" style="width:95px;height:32px;">
									<s:if test='ordertype=="value_change"'>
										<option value="regular_score">保有优先</option>
										<option value="value_change" selected ="selected">价值优先</option>
									</s:if>
									<s:else>
										<option value="regular_score" selected ="selected">保有优先</option>
										<option value="value_change" >价值优先</option>
									</s:else>
								</select>
							</th>
							<th>手机号</th>
							<th>合约开始时间</th>
							<th>合约到期时间</th>
							<!--th>合约剩余</th-->
							<th>当前套餐</th>
							<th>用户状态</th>
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
							<!--td>  
								<s:if test='ordertype=="value_change"'>
									<s:property value="valueChangeDesc"/>
								</s:if>
								<s:else>
									<s:property value="priorityDesc"/>
								</s:else>
							</td-->
									
							<td><s:property value="phoneNo" /></td>

							<s:if test="contractFrom.longValue() == 0">
								<td>——</td>
							</s:if>
							<s:else>
								<td><s:property value="contractFrom.longValue()" /></td>
							</s:else>

							<s:if test="contractTo.longValue() == 0">
								<td>——</td>
							</s:if>
							<s:else>
								<td><s:property value="contractTo.longValue()" /></td>
							</s:else>

							<!--td>合约剩余</td-->
							<td><s:property value="packageName" /></td>
							<td><s:property value="status"/></td>
							<td><a class="btn" href="javascript:void(0)"
								onclick="showDetail('<s:property value="packageName"/>');ajax_get_consumer_data(<s:property value='phoneNo'/>);">查看</a>
							</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
			<!-- /.mod-bd -->
			<s:if test="null!=bills&&!bills.isEmpty()">
				<div class="ft">
					<pg:pager>
						<pg:index export="totalItems = itemCount">
							<div class="pager">
								<pg:first>
									<a class="start"
										href="getSingleHighGprsConsumerBills?currPage=1&theMonth=<s:property value='theMonth'/>&ordertype=<s:property value="ordertype"/>">首页</a>
								</pg:first>
								<pg:prev>
									<s:if test="prevPage<=0">
										<span class="page current">上一页</span>
									</s:if>
									<s:else>
										<a class="prev"
											href="getSingleHighGprsConsumerBills?currPage=<s:property value="prevPage"/>&theMonth=<s:property value="theMonth"/>&ordertype=<s:property value="ordertype"/>">上一页</a>
									</s:else>
								</pg:prev>
								<pg:pages>
									<span class="info">第<s:property value="currPage" />页(共<s:property
											value="totalNumber" />条/<s:property value="totalPages" />页)
									</span>
								</pg:pages>
								<pg:next>
									<s:if test="nextPage<=0">
										<span class="page current">下一页</span>
									</s:if>
									<s:else>
										<a class="next"
											href="getSingleHighGprsConsumerBills?currPage=<s:property value="nextPage"/>&theMonth=<s:property value="theMonth"/>&ordertype=<s:property value="ordertype"/>">下一页</a>
									</s:else>
								</pg:next>
								<pg:last>
									<a class="end"
										href="getSingleHighGprsConsumerBills?currPage=<s:property value="totalPages"/>&theMonth=<s:property value="theMonth"/>&ordertype=<s:property value="ordertype"/>">末页</a>
								</pg:last>
							</div>
							<!-- /.pager -->
						</pg:index>
					</pg:pager>
				</div>
				<!-- /.mod-ft -->
			</s:if>
		</div>
		<!-- /.mod -->

		<div id="w" style="padding: 5px">
			<!--class="easyui-window" title="<i class='icon icon-inbox'></i>用户消费详情" collapsible="false" minimizable="false" 
		 data-options="modal:true,closed:true" style="width:90%;height:500px;padding:10px;"-->
			<div style="vertical-align:bottom">
				<div style="float: left; ">
					<b><span id="currUser" style="font-size: 18px;"></span></b>
				</div>
				<div style="text-align: right; float: left; width: 35%; ">
					<b><span id="detailInfo" style="font-size: 18px;"></span></b>
				</div>
				<div style="float: right; width: 100px;display:none">
					<input type="checkbox" name="ck_all" id="ck" checked=true /><span
						style="font-size: 13px;">显示近期消费</span>
				</div>
				<div style="text-align: right; float: right;  ">
					<b><span id="packageInfo" style="font-size: 18px;"></span></b>
				</div>
			</div>
			<div>
				<table id="tt" title="消费细节" style="width: 100%; height: 80px">
				</table>
			</div>
			<div>
				<table id="textend" title="波动信息" style="width: 100%; ">
				</table>
			</div>
			<!--div>
				<div>
					<b><span style="font-size: 18px;">套餐推荐</span></b>
				</div>
				<div>
					<ul style="font-size:13px; ">
						<li class="liout" style="color: #FF6633;font-weight: bold;"><span id="rcmd1"></span></li>
						<li class="liout"><span id="rcmd2"></span></li>
						<li class="liout"><span id="rcmd3"></span></li>
					</ul>				
				</div>
			</div-->
			<!--div style="float: left; padding: 5px;width: 80%; ">
					<input class="textbox" id="phone"
						style="border-radius: 3px; width: 80%; font-size: 14px; padding: 7px"
						placeholder="请简单填写实际采取的措施，如电话沟通，实际套餐推荐">&nbsp;&nbsp;
			</div-->
			<div style="margin:10px">
			 <a class="btn" href="javascript:void(0)" >流量套餐推荐</a>
			</div>
			<div style="margin:10px">
				<a class="btn" href="javascript:void(0)">加油包推荐</a>
			</div>
			<div style="margin:10px">
				<a class="btn" href="javascript:void(0)">带宽升级-3G到4G</a>
			</div>
			<div style="float: right; padding: 5px">
				<!--a class="btn" href="javascript:void(0)"
					onclick="$('#w').window('close')">确定并关闭</a-->
				<a class="btn" href="javascript:void(0)"
					onclick="$('#w').window('close')">关闭</a>
			</div>

			<div id="ccc"></div>
		</div>

	</div>
	<!-- /.container  -->

	<script type="text/javascript" src="admin/assets/js/bizSingleGprs.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#filt').bind('click', function(e){
				yearMonth = $('#datePicker').val().split('-');
				theMonth = yearMonth[0] + yearMonth[1] + yearMonth[2];
				order = $('#ordertype').combobox('getValue');
				phoneNo = $('#phone').val();
				window.location.href = "getSingleHighGprsConsumerBills?theMonth=" + theMonth + "&phoneNo=" + phoneNo + "&ordertype="+order;
			});
		});
		
		__scroll__ = 0;
		
		function showDetail(packageName){
			__scroll__ = $(document).scrollTop();
			$('#packageInfo').text("套餐：" + packageName);
			
			$('#w').window({
				title: "消费详情",
				collapsible: false,
				minimizable: false,
				maximizable: false,
				draggable: false,
				resizable: false,
				shadow: false,
				maximized: true,
				
				onBeforeClose:function(){
					if(window.addEventListener){  
						$(document).scrollTo(__scroll__, 0);
					} else if(window.attachEvent){   
						$(window).scrollTo(__scroll__, 0);
				    }
				}
			});	
		};
		
		$(function(){
			$('#w').window({
				title: "消费详情",
				collapsible: false,
				minimizable: false,
				maximizable: false,
				draggable: false,
				resizable: false,
				shadow: false,
				maximized: true
			});
			$('#w').window("close");
		});
		
			$('#ordertype').combobox({
				panelHeight:"50px",
				editable:false,
			    onChange:function(newValue,oldValue){
			        yearMonth = $('#datePicker').val().split('-');
					theMonth = yearMonth[0] + yearMonth[1] + yearMonth[2];
					order = $('#ordertype').combobox('getValue');
					phoneNo = $('#phone').val();
					window.location.href = "getSingleHighGprsConsumerBills?theMonth=" + theMonth + "&phoneNo=" + phoneNo + "&ordertype="+order;
			    }
			});
		
	</script>

</body>
</html>


