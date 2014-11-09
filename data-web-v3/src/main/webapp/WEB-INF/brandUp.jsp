<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>IntellBit客户保有工具</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="css/plugins/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/sb-admin-2.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="css/plugins/morris.css" rel="stylesheet">
    
    <link href='css/plugins/datePicker.css'  rel='stylesheet'/>

    <!-- Custom Fonts -->
    <link href="font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="js/html5shiv.js"></script>
        <script src="js/respond.js"></script>
    <![endif]-->
    
    <style>
    	.pagination {
    		margin:0;
    		float:right;
    	}
    </style>

</head>

<body>

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="my">IntellBit客户保有工具</a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" style="font-size:16px">
                        <i class="fa fa-user fa-fw"></i><%=session.getAttribute("realname")%>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <!-- <li><a href="#"><i class="fa fa-user fa-fw"></i> 我的信息</a>
                        </li>
                        <li><a href="#"><i class="fa fa-gear fa-fw"></i> 设置</a>
                        </li>
                        <li class="divider"></li> -->
                        <li><a href="logout"><i class="fa fa-sign-out fa-fw"></i> 退出</a>
                        </li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
            </ul>
            <!-- /.navbar-top-links -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li>
                            <a href="my"><i class="fa fa-dashboard fa-fw"></i> 我的工作台</a>
                        </li>
                       <!--   <li>
                            <a href="customers"><i class="fa fa-table fa-fw"></i> 我的客户</a>
                        </li> -->
                        <li>
                            <a href="contractTask"><i class="fa fa-table fa-fw"></i> 续约任务</a>
                        </li>
						<li>
                            <a href="#"><i class="fa fa-sitemap fa-fw"></i> 专项任务<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level collapse in">
                                <li>
                                    <a class="active" href="brandUp">4G带宽升级</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-cog fa-fw"></i> 我的设置<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
									<%
										Object group = session.getAttribute("group");
										if (group != null && group.toString().equals("0")) {
									%> 
									<a href="userManager">用户管理</a> 
									<%
									 	}
									 %> 
									 <a href="myProfile">我的信息</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>
        
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">带宽升级
						<span class="label label-info" style="font-size:14px;right:240px;position:absolute;margin-top:10px">请选择日期：</span>						
						<div class="dtPicker" style="display:inline;right:20px;position:absolute;margin-top:5px">
							<input type="text" class="form-control" style="width:200px;height:30px;float:right;margin-bottom:5px;cursor:pointer;background-color:#ffffff" readonly="true"/>
			            </div>
		            </h1>   
             	</div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                        	带宽升级用户
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="dt-brandup">
                                    <thead>
									    <tr class="success">
									    	<th>手机号</th>
									        <!--th>优先级</th-->
									        <th>
									        	<select id="orderby" class="form-control" style="font-size:9px">
                                                    <option value="gprs6">流量优先</option>
                                                    <option value="value_change_4g">推荐价值</option>
                                                </select>
                                            </th>
									        <th>当月收入</th>
									        <th>近6月平均收入</th>
									        <th>当月流量(M)</th>
									        <th>近6月平均流量(M)</th>
									        <th>当前套餐</th>
									        <th>合约用户</th>
									        <!-- <th>任务状态</th> -->
									        <th>操作</th>
									    </tr>
									    </thead>
									    <tbody>
									    
									    </tbody>
                                </table>
                            </div>
                            <!-- /.table-responsive -->
                            <!--div class="well">
                                <h4>DataTables Usage Information</h4>
                                <p>DataTables is a very flexible, advanced tables plugin for jQuery. In SB Admin, we are using a specialized version of DataTables built for Bootstrap 3. We have also customized the table headings to use Font Awesome icons in place of images. For complete documentation on DataTables, visit their website at <a target="_blank" href="https://datatables.net/">https://datatables.net/</a>.</p>
                                <a class="btn btn-default btn-lg btn-block" target="_blank" href="https://datatables.net/">View DataTables Documentation</a>
                            </div-->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery Version 1.11.0 -->
    <script src="js/jquery-1.11.0.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/plugins/metisMenu/metisMenu.min.js"></script>

    <!-- DataTables JavaScript -->
    <script src="js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>
    
    <script src="js/plugins/bootstrap-datepicker.js"></script>
    
    <script src="js/plugins/popover.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/sb-admin-2.js"></script>
    
    <script src="js/intellbi.biz.js"></script>

    <!-- Page-Level Demo Scripts - Tables - Use for reference -->

</body>

</html>
