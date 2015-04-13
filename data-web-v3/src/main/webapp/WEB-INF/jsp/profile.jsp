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
                        <!-- <li><a href="#"><i class="fa fa-user fa-fw"></i> 我的信息</a>
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
                       <li>
                            <a href="maintain.html"><i class="fa fa-eject fa-fw"></i> 保有任务</a>
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
                            <ul class="nav nav-second-level collapse in">
                                <li>
                                    <c:if test="${group=='0'}"><a href="userManager.html">用户管理</a> </c:if>
                                     <a class="active" href="profile.html">我的信息</a>
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
                    <h1 class="page-header">我的信息
		            </h1>   
             	</div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                        	密码管理
                        </div>
                        <!-- /.panel-heading -->
						<div class="panel-body">

							<div class="form-group has-success">
								<label class="control-label" for="inputSuccess">请输入老密码：</label>
								<input type="password" class="form-control" id="oldpasswd"
									style="width: 300px">
							</div>
							<div class="form-group has-error">
								<label class="control-label" for="inputSuccess">请输入新密码：</label>
								<input type="password" class="form-control" id="newpasswd1"
									style="width: 300px">
							</div>
							<div class="form-group has-error">
								<label class="control-label" for="inputSuccess">请确认新密码</label> <input
									type="password" class="form-control" id="newpasswd2"
									style="width: 300px">
							</div>

							<div id="screen" class="alert alert-danger" style="display:none">
								<span id="msg"> </span>
							</div>

								<a id="ok" class="btn btn-success">提交</a>
								
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
    
    <!-- <script src="js/intellbi.biz.js"></script> -->

    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
    
    <script type="text/javascript">
		$('#ok').bind('click', function(e){
			passwd = $('#newpasswd2').val();
			oldpasswd = $('#oldpasswd').val();
			if($('#newpasswd1').val() != $('#newpasswd2').val()) {
				$('#screen').css('display','block');
				$('#msg').html('两次输入的新密码不一样');
			}
			else {
				$.ajax({
					  type: "POST",
					  url: "/api/setPassword",
					  "contentType": "application/x-www-form-urlencoded",
	                  "dataType": "json",
					  data: { "password": passwd, "oldpassword": oldpasswd},
					  success:function(data){
							$('#screen').css('display','block');
					        $('#msg').html(data['data']);
					  }
				});
			}
		});
		
		$('#newpasswd1').bind('change', function(e){
			if($('#newpasswd1').val() != $('#newpasswd2').val()) {
				$('#screen').css('display','block');
				$('#msg').html('两次输入的新密码不一样');
			}
			else
				$('#msg').html('');
		});
		
		$('#newpasswd2').bind('change', function(e){
			if($('#newpasswd1').val() != $('#newpasswd2').val()){ 
				$('#screen').css('display','block');
				$('#msg').html('两次输入的新密码不一样');
			}
			else
				$('#msg').html('');
		});
		
    </script>

</body>

</html>
