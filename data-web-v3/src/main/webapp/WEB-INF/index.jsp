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

    <!-- Custom Fonts -->
    <link href="font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="js/html5shiv.js"></script>
        <script src="js/respond.js"></script>
    <![endif]-->

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
                        <li><a href="#"><i class="fa fa-user fa-fw"></i> 我的信息</a>
                        </li>
                        <li><a href="#"><i class="fa fa-gear fa-fw"></i> 设置</a>
                        </li>
                        <li class="divider"></li>
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
                            <a class="active" href="my"><i class="fa fa-dashboard fa-fw"></i> 我的工作台</a>
                        </li>
                        <!-- <li>
                            <a href="customers"><i class="fa fa-table fa-fw"></i> 我的客户</a>
                        </li> -->
                        <li>
                            <a href="contractTask"><i class="fa fa-table fa-fw"></i> 续约任务</a>
                        </li>
                        
                        <li>
                            <a href="#"><i class="fa fa-sitemap fa-fw"></i> 专项任务<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="brandUp">4G带宽升级</a>
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
                    <h1 class="page-header">我的工作台</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <%-- <div class="row">
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-comments fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="huge">26</div>
                                    <div>New Comments!</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-green">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-tasks fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="huge">12</div>
                                    <div>New Tasks!</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-yellow">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-shopping-cart fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="huge">124</div>
                                    <div>New Orders!</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-red">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-support fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="huge">13</div>
                                    <div>Support Tickets!</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
            </div> --%>


			<div class="panel panel-primary">
				<div class="panel-heading">任务概览</div>
				<div class="panel-body">
				
				<div class="box-content row">
	                <div class="col-lg-7 col-md-12">
	                    <h2><%=session.getAttribute("realname")%>，您好！
	                        <small>今天是<span id="timedate"></span></small>
	                    </h2>
	                    <p>当前您的名下有
	                        <a data-toggle="tooltip" data-placement="top" title="点击查看" href="http://www.baidu.com" target="_blank">
	                            <big><strong><s:property value="vipNumber"/>10</strong></big>
	                        </a>位VIP客户，其中：
	                    </p>             
	                    <p>
	                        &nbsp;&nbsp;&nbsp;&nbsp;合约用户
	                        <a data-toggle="tooltip" data-placement="top" title="点击查看" href="http://www.baidu.com" target="_blank">
	                            <big><strong><s:property value="vipNumber"/>10</strong></big>
	                        </a>位，请进入
	                        <a data-toggle="tooltip" data-placement="top" title="点击进入合约用户保有任务" href="contractTask">
	                            <big><b>保有任务</b></big>
	                        </a>菜单管理进行管理：
	                    </p>
	                    <ul>
	                        <li><b>本日（<span id="timedate-day"></span>）</b>待处理的合约用户位，已处理位，剩余位</li>
	                        <li><b>本周（<span id="timedate-week-start"></span>~<span id="timedate-week-end"></span>）</b>待处理的合约用户位，已处理位，剩余位</li>
		                    <li><b>本月（<span id="timedate-month-start"></span>~<span id="timedate-month-end"></span>）</b>待处理的合约用户位，已处理位，剩余位</li>
	                    </ul>
	                    
	                    <p>
	                        &nbsp;&nbsp;&nbsp;&nbsp;单高流量用户
	                        <a data-toggle="tooltip" data-placement="top" title="点击查看" href="http://www.baidu.com" target="_blank">
	                            <big><strong><s:property value="vipNumber"/>10</strong></big>
	                        </a>位，请进入
	                        <a data-toggle="tooltip" data-placement="top" title="点击进入专项任务之单高流量用户" href="http://www.baidu.com">
	                            <big><b>专项任务&nbsp;/&nbsp;单高流量用户</b></big>
	                        </a>任务菜单进行管理.
	                    </p>
	                    <!--p class="center-block download-buttons">
	                        <a href="http://usman.it/free-responsive-admin-template/" class="btn btn-primary btn-lg"><i
	                                class="glyphicon glyphicon-chevron-left glyphicon-white"></i> Back to article</a>
	                        <a href="http://usman.it/free-responsive-admin-template/" class="btn btn-default btn-lg"><i
	                                class="glyphicon glyphicon-download-alt"></i> Download Page</a>
	                    </p-->
	                </div>
            	</div>
            
				</div>
				<div class="panel-footer"> </div>
			</div>

            <!-- /.row -->
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

    <!-- Custom Theme JavaScript -->
    <script src="js/sb-admin-2.js"></script>
    
    <script src="js/intellbi.biz.js"></script>

</body>

</html>
