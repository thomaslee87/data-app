<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
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

    <link rel="stylesheet" type="text/css" href="css/plugins/dataTables.bootstrap.css"">

    <!-- MetisMenu CSS -->
    <link href="css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="css/plugins/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/common.css" rel="stylesheet">

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
                <a class="navbar-brand" href="index.html">IntellBit客户保有工具</a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" style="font-size:16px">
                        <i class="fa fa-user fa-fw"></i><c:out value="${realname}"/>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                       <!--  <li><a href="#"><i class="fa fa-user fa-fw"></i> 我的信息</a>
                        </li>
                        <li><a href="#"><i class="fa fa-gear fa-fw"></i> 设置</a>
                        </li>
                        <li class="divider"></li> -->
                        <li><a href="logout.html"><i class="fa fa-sign-out fa-fw"></i> 退出</a>
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
                            <a href="index.html"><i class="fa fa-dashboard fa-fw"></i> 我的工作台</a>
                        </li>
                       <!--   <li>
                            <a href="customers"><i class="fa fa-table fa-fw"></i> 我的客户</a>
                        </li> -->
                        <li>
                            <a class="active" href="maintain.html"><i class="fa fa-eject fa-fw"></i> 保有任务</a>
                        </li>
                        <li>
                            <a href="contract.html"><i class="fa fa-refresh fa-fw"></i> 续约任务</a>
                        </li>
						<li>
                            <a href="#"><i class="fa fa-sitemap fa-fw"></i> 专项任务<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="bandwidth.html">4G带宽升级</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-cog fa-fw"></i> 我的设置<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                              	  	<c:if test="${group=='0'}"><a href="userManager.html">用户管理</a> </c:if>
									 <a href="profile.html">我的信息</a>
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
                    <h1 class="page-header">保有任务
                    	<!-- <div style="display:inline" id="taskView">
		                    <label class="radio-inline" style="font-size:14px"><input type="radio"  name="inlineRadioOptions" id="rDay" value="day" style="margin-top:1px"> 日视图</label>
							<label class="radio-inline" style="font-size:14px"><input type="radio" name="inlineRadioOptions" id="rWeek" value="week" style="margin-top:1px"> 周视图</label>
							<label class="radio-inline" style="font-size:14px"><input type="radio" checked name="inlineRadioOptions" id="rMonth" value="month" style="margin-top:1px"> 月视图</label>
						</div> -->
						<div style="display:inline;font-size:12px;right:350px;position:absolute;margin-top:10px" >
                            <input type="checkbox" id="hideDone" checked>隐藏已处理的客户
						</div>
						<span class="label label-primary" style="font-size:14px;right:240px;position:absolute;margin-top:10px">请选择日期：</span>						
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
                    <div id="J-main-panel" class="panel panel-primary">
                        <div class="panel-heading">
                        	合约用户
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table cellpadding="0" cellspacing="0" border="0" class="table table-striped  table-hover"" id="J-table-maintain">
                                    <thead>
									    <tr class="success">
									    	<th>手机号</th>
									        <!--th>优先级</th-->
									        <th style="display:none">
									        	<select id="orderby" class="form-control" style="font-size:9px;">
                                                    <option value="regular_score">保有优先</option>
                                                    <option value="value_change">价值优先</option>
                                                </select>
                                            </th>
                                            <th style="width:90px">保有优先级</th>
                                            <!-- <th>续约价值</th> -->
									        <th>合约开始</th>
									        <th>合约结束</th>
									        <!--th>合约剩余</th-->
									        <th>当前套餐</th>
									        <th>任务状态</th>
									        <th>操作</th>
									    </tr>
									    </thead>
									    
        
									    <tbody>
									    
									    </tbody>
                                </table>
                                
                                <!-- Modal -->
								<div class="modal fade" id="J-dg-modal" tabindex="-1"
									role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
									<div class="modal-dialog">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal"
													aria-hidden="true">&times;</button>
												<h4 class="modal-title" id="myModalLabel">高级搜索
													<span class="inline-info">&nbsp;&nbsp;条件间为“与”的关系，并且允许为空</span>
												</h4>
											</div>
											<div class="modal-body">

												<form class="form-horizontal" role="form">
													<div class="form-group">
														<label class="col-sm-2 control-label">手机号</label>
														<div class="col-sm-10">
															<input class="form-control" id="J-cond-phone" style="width:45%" placeholder="输入手机号">
														</div>
													</div>
													<div class="form-group">
														<label for="inputPassword" class="col-sm-2 control-label">合约开始</label>
														<div class="col-sm-10">
															<input class="form-control form-control-inline" id="J-cond-from-start" style="width:45%" placeholder="日期格式 yyyymmdd">
															~
															<input class="form-control form-control-inline" id="J-cond-from-end" style="width:45%" placeholder="日期格式 yyyymmdd">
														</div>
													</div>
													<div class="form-group">
														<label for="inputPassword" class="col-sm-2 control-label">合约结束</label>
														<div class="col-sm-10">
															<input class="form-control form-control-inline" id="J-cond-to-start" style="width:45%" placeholder="日期格式 yyyymmdd">
															~
															<input class="form-control form-control-inline" id="J-cond-to-end" style="width:45%" placeholder="日期格式 yyyymmdd">
														</div>
													</div>
													<div class="form-group">
														<label for="" class="col-sm-2 control-label">套餐金额</label>
														<div class="col-sm-10">
															<input class="form-control form-control-inline" id="J-cond-price" style="width:45%" placeholder="输入套餐金额">
															<span class="form-control-inline">&nbsp;元</span>
														</div>
													</div>
												</form>

											</div>
											<div class="modal-footer">
												<button type="button" class="btn btn-default"
													data-dismiss="modal">取消</button>
												<button id='J-btn-search' type="button" class="btn btn-primary">确认
												</button>
											</div>
										</div>
										<!-- /.modal-content -->
									</div>
									<!-- /.modal -->


								</div>
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
    <script src="js/common.js"></script>
    
    <script src="js/biz.maintain.js"></script>

    <!-- Page-Level Demo Scripts - Tables - Use for reference -->

</body>

</html>
