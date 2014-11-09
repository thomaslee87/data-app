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

<link href='css/plugins/datePicker.css' rel='stylesheet' />

<!-- Custom Fonts -->
<link href="font-awesome-4.1.0/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="js/html5shiv.js"></script>
        <script src="js/respond.js"></script>
    <![endif]-->

<style>
.pagination {
	margin: 0;
	float: right;
}
</style>

</head>

<body>

	<div id="wrapper">

		<!-- Navigation -->
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="my">IntellBit客户保有工具</a>
			</div>
			<!-- /.navbar-header -->

			<ul class="nav navbar-top-links navbar-right">
				<!-- /.dropdown -->
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#" style="font-size: 16px"> <i
						class="fa fa-user fa-fw"></i><%=session.getAttribute("realname")%>
						<i class="fa fa-caret-down"></i>
				</a>
					<ul class="dropdown-menu dropdown-user">
						<!-- <li><a href="#"><i class="fa fa-user fa-fw"></i> 我的信息</a></li>
						<li><a href="#"><i class="fa fa-gear fa-fw"></i> 设置</a></li>
						<li class="divider"></li> -->
						<li><a href="logout"><i class="fa fa-sign-out fa-fw"></i>
								退出</a></li>
					</ul> <!-- /.dropdown-user --></li>
				<!-- /.dropdown -->
			</ul>
			<!-- /.navbar-top-links -->

			<div class="navbar-default sidebar" role="navigation">
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">
						<li><a class="active" href="my"><i
								class="fa fa-dashboard fa-fw"></i> 我的工作台</a></li>
						<li><a class="active" href="#"><i
								class="fa fa-table fa-area-chart"></i> 消费详情</a></li>
					</ul>
				</div>
				<!-- /.sidebar-collapse -->
			</div>
			<!-- /.navbar-static-side -->
		</nav>

		<div id="page-wrapper">

			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						消费细节
						<!--span class="label label-info" style="font-size:14px;right:240px;position:absolute;margin-top:10px">请选择日期：</span>						
						<div class="dtPicker" style="display:inline;right:20px;position:absolute;margin-top:5px">
							<input type="text" class="form-control" style="width:200px;height:30px;float:right;margin-bottom:5px;cursor:pointer;background-color:#ffffff" readonly="true"/>
			            </div-->
					</h1>
				</div>
				<!-- /.col-lg-12 -->
			</div>

			<div class="row">
				<div class="col-lg-12">
					<div style="display: inline">
						<div class="alert alert-warning"
							style="margin-top: 5px; margin-bottom: 10px">
							<span>客户号码：<big><b> <s:property value="phoneNo" /></b></big></span>
							<span>&nbsp;&nbsp;</span> <span>当前套餐：<big><b><s:property
											value="currPkgName" /></b></big></span>
						</div>
					</div>
				</div>
			</div>

			<ul class="nav nav-tabs">
				<li class="active"><a href="#home" data-toggle="tab">消费信息</a></li>
				<li><a href="#profile" data-toggle="tab">近期趋势</a></li>
			</ul>

			<div class="tab-content">
				<div class="tab-pane fade in active" id="home">
					<div class="row">
						<div class="col-lg-15">
							<div class="panel panel-default" style="margin-top: 10px">
								<div class="panel-heading">
									<b>消费详情</b>
								</div>
								<!-- /.panel-heading -->
								<div class="panel-body">
									<div class="table-responsive col-lg-15">
										<table class="table table-striped table-bordered table-hover"
											id="dt-bills">
											<thead>
												<tr class="info">
													<th>月份</th>
													<th>收入</th>
													<th>月租</th>
													<th>本地</th>
													<th>漫游</th>
													<th>长话</th>
													<th>增值费</th>
													<th>其他费</th>
													<th>赠款</th>
													<th>通话次数</th>
													<th>本地时长(分)</th>
													<th>漫游时长(分)</th>
													<th>长途时长(分)</th>
													<!-- <th>国内长途时长</th>
									        		<th>国际长途时长</th> -->
													<th>短信</th>
													<th>流量(M)</th>
												</tr>
											</thead>
											<tbody>
												<s:iterator value="recentBillList" status="status">
													<s:if test="#status.index <= 5">
														<tr>
															<td class='month'><s:property value="yearMonth" /></td>
															<td class='income'><s:property value="income" /></td>
															<td><s:property value="monthlyRental" /></td>
															<td class='local'><s:property value="localVoiceFee" /></td>
															<td class='roam'><s:property value="roamingFee" /></td>
															<td class='long'><s:property
																	value="longDistanceVoiceFee" /></td>
															<td class='valued'><s:property value="valueAddedFee" /></td>
															<td><s:property value="otherFee" /></td>
															<td><s:property value="grantFee" /></td>
															<td class='callnum'><s:property value="callNumber" /></td>
															<td><s:property value="localCallDuration" /></td>
															<td><s:property value="roamCallDuration" /></td>
															<td><s:property value="longDistanceCallDuration" /></td>
															<td><s:property value="sms" /></td>
															<td class='gprs'><s:property value="gprs" /></td>
														</tr>
													</s:if>
												</s:iterator>

											</tbody>
										</table>
									</div>

									<div class="table-responsive">
										<table class="table table-striped table-bordered table-hover"
											id="dt-extra">
											<thead>
												<tr class="info">
													<th>保有优先级</th>
													<th>近6月平均arpu值</th>
													<th>近3月平均arpu值</th>
													<th>收入套餐比</th>
													<th>收入波动水平</th>
													<th>近6月平均流量(M)</th>
													<th>近3月平均流量(M)</th>
													<th>流量套餐额度比</th>
													<th>流量波动水平</th>
												</tr>
											</thead>
											<tbody>
												<s:iterator value="recentBillList" status="status">
													<s:if test="#status.index <= 0">
														<tr>
															<td><s:property value="priorityDesc" /></td>
															<td><s:property value="income6" /></td>
															<td><s:property value="income3" /></td>
															<td><s:property value="packageSpill" /></td>
															<td><s:property value="incomeFluctuation" /></td>
															<%-- <td><s:property value="voice6" /></td>
																<td><s:property value="voice3" /></td>
																<td><s:property value="voiceSpill" /></td>
																<td><s:property value="voiceFluctuation" /></td> --%>
															<td><s:property value="gprs6" /></td>
															<td><s:property value="gprs3" /></td>
															<td><s:property value="gprsSpill" /></td>
															<td><s:property value="gprsFluctuation" /></td>
														</tr>
													</s:if>
												</s:iterator>
											</tbody>
										</table>
									</div>

									<div class="panel panel-default" style="margin-top: 10px">
										<div class="panel-heading">
											<b>消费结构</b>
										</div>
										<div class="panel-body">
											<div class="col-lg-6">
											<span>当月消费结构：</span>
												<div class="flot-chart-content"
													style="width: 400px; height: 200px" id="pie-recent"></div>
											</div>

											<div class="col-lg-6">
											<span>近6个月平均消费结构：</span>
												<div class="flot-chart-content"
													style="width: 400px; height: 200px" id="pie-avg"></div>
											</div>
										</div>
									</div>
								</div>
							</div>
							</div>
						</div>

						<div class="row">
							<div class="col-lg-15">
								<div class="panel panel-success">
									<div class="panel-heading">
										<b>任务相关</b>
								</div>
									<!-- .panel-heading -->
									<div class="panel-body">
										<div class="panel-group" id="accordion">
											<div class="panel panel-default">
												<div class="panel-heading">
													<h4 class="panel-title">
														<a data-toggle="collapse" data-parent="#accordion"
															href="#">3G套餐推荐</a>
													</h4>
											</div>
												<div id="collapseOne" class="panel-collapse collapse in">

													<ul class="list-group">
														<s:if test="recommend1 == '' or recommendSave1 <= 0">
															<li class="list-group-item">暂无推荐方案</li>
														</s:if>
														<s:else>
															<li class="list-group-item"><span
																class="label label-warning">荐</span>
																	<button type="button" class="btn btn-danger btn-xs" data-toggle="popover-left" data-content='<s:property value="rcmd1pkg"/>' title="" data-original-title="<center><span class=&quot;label label-warning&quot;>套餐资费</span></center>">	
																		<s:property value="recommend1" />
																	</button>
																	预计平均每月消费<s:property value="recommendCost1" />元，
																	 平均每月可节省<s:property value="recommendSave1" />元</li>
															<s:if test="recommend2 != '' && recommendSave2 > 0">
																<li class="list-group-item"><span
																	class="label label-warning">荐</span> 
																	<button type="button" class="btn btn-danger btn-xs" data-toggle="popover-left" data-content='<s:property value="rcmd2pkg"/>' title="" data-original-title="<center><span class=&quot;label label-warning&quot;>套餐资费</span></center>">	
																		<s:property value="recommend2" />
																	</button>
																	 预计平均每月消费<s:property value="recommendCost2" />元，
																	  平均每月可节省<s:property value="recommendSave2" />元</li>
															</s:if>
															<s:if test="recommend3 != '' && recommendSave3 > 0">
																<li class="list-group-item"><span
																	class="label label-warning">荐</span> 
																	<button type="button" class="btn btn-danger btn-xs" data-toggle="popover-left" data-content='<s:property value="rcmd3pkg"/>' title="" data-original-title="<center><span class=&quot;label label-warning&quot;>套餐资费</span></center>">	
																		<s:property value="recommend3" />
																	</button>
																	 预计平均每月消费<s:property value="recommendCost3" />元，
																	  平均每月可节省<s:property value="recommendSave3" />元</li>
															</s:if>
														</s:else>
													</ul>

												</div>
											</div>
											<div class="panel panel-default">
												<div class="panel-heading">
													<h4 class="panel-title">
														<a data-toggle="collapse" data-parent="#accordion"
															href="#">4G带宽升级</a>
													</h4>
												</div>
												<div id="collapseTwo" class="panel-collapse collapse in">
												<ul class="list-group">
														<li class="list-group-item"><span
															class="label label-warning">荐</span>
																<button type="button" class="btn btn-danger btn-xs" data-toggle="popover-left" data-content='<s:property value="rcmd1pkg4G"/>' title="" data-original-title="<center><span class=&quot;label label-warning&quot;>4G套餐资费</span></center>">	
																	（4G）<s:property value="recommend1_4G" />
																</button>
																预计平均每月消费<s:property value="recommendCost1_4G" />元，
																<s:if test="recommendSave1_4G > 0"> 
																 平均每月可节省<s:property value="recommendSave1_4G" />元
																 </s:if>
																 <s:else>
																 平均每月多消费<s:property value="-1*recommendSave1_4G" />元，可享受更快的4G网速。
																 </s:else>
																 </li>
														<s:if test="recommend2_4G != '' && recommendSave2_4G > 0">
															<li class="list-group-item"><span
																class="label label-warning">荐</span> 
																<button type="button" class="btn btn-danger btn-xs" data-toggle="popover-left" data-content='<s:property value="rcmd2pkg4G"/>' title="" data-original-title="<center><span class=&quot;label label-warning&quot;>4G套餐资费</span></center>">	
																	（4G）<s:property value="recommend2_4G" />
																</button>
																 预计平均每月消费<s:property value="recommendCost2_4G" />元，
																  平均每月可节省<s:property value="recommendSave2_4G" />元</li>
														</s:if>
														<s:if test="recommend3_4G != '' && recommendSave3_4G > 0"> 
															<li class="list-group-item"><span
																class="label label-warning">荐</span> 
																<button type="button" class="btn btn-danger btn-xs" data-toggle="popover-left" data-content='<s:property value="rcmd3pkg4G"/>' title="" data-original-title="<center><span class=&quot;label label-warning&quot;>4G套餐资费</span></center>">	
																	（4G）<s:property value="recommend3_4G" />
																</button>
																 预计平均每月消费<s:property value="recommendCost3_4G" />元，
																  平均每月可节省<s:property value="recommendSave3_4G" />元</li>
														</s:if>
													</ul>

												</div>
											</div>
										</div>
									</div>
									<!-- .panel-body -->
								</div>
								<!-- /.panel -->
							</div>
						</div>
					</div>
					<div class="tab-pane fade" id="profile">
						<div class="row" style="margin-top: 20px">
							<div class="col-lg-6">
								<div class="panel panel-default">
									<div class="panel-heading">收入变化趋势</div>
									<div class="panel-body">
										<div class="flot-chart">
											<div class="flot-chart-content" id="income-placeholder"></div>
										</div>
									</div>
								</div>
							</div>

							<div class="col-lg-6">
								<div class="panel panel-default">
									<div class="panel-heading">GPRS流量变化趋势</div>
									<!-- /.panel-heading -->
									<div class="panel-body">
										<div class="flot-chart">
											<div class="flot-chart-content" id="gprs-placeholder"></div>
										</div>
									</div>
								</div>
							</div>

							<div class="col-lg-6">
								<div class="panel panel-default">
									<div class="panel-heading">语音费用（本地+长途+漫游）变化趋势</div>
									<!-- /.panel-heading -->
									<div class="panel-body">
										<div class="flot-chart">
											<div class="flot-chart-content" id="voice-placeholder"></div>
										</div>
									</div>
								</div>
							</div>

							<div class="col-lg-6">
								<div class="panel panel-default">
									<div class="panel-heading">通话次数变化趋势</div>
									<!-- /.panel-heading -->
									<div class="panel-body">
										<div class="flot-chart">
											<div class="flot-chart-content" id="callnumber-placeholder"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

	<!-- jQuery Version 1.11.0 -->
	<script src="js/jquery-1.11.0.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="js/plugins/metisMenu/metisMenu.min.js"></script>

	<!-- DataTables JavaScript -->
	<script src="js/plugins/dataTables/jquery.dataTables.js"></script>
	<script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>

	<%-- <script src="js/plugins/bootstrap-datepicker.js"></script> --%>

	<%-- <script src="js/plugins/popover.js"></script> --%>

	<!-- Flot Charts JavaScript -->
	<script src="js/plugins/flot/excanvas.min.js"></script>
	<script src="js/plugins/flot/jquery.flot.js"></script>
	<script src="js/plugins/flot/jquery.flot.pie.js"></script>
	<script src="js/plugins/flot/jquery.flot.resize.js"></script>
	<script src="js/plugins/flot/jquery.flot.axislabels.js"></script>
	<script src="js/plugins/flot/jquery.flot.tooltip.min.js"></script>

	<!-- Custom Theme JavaScript -->
	<script src="js/sb-admin-2.js"></script>

	<%-- <script src="js/intellbi.biz.js"></script> --%>

	<!-- Page-Level Demo Scripts - Tables - Use for reference -->

	<script type="text/javascript">
		$(document).ready(function() {
			
			_local = parseFloat($('#dt-bills tr .local')[0].innerHTML);
			_roam = parseFloat($('#dt-bills tr .roam')[0].innerHTML);
			_long = parseFloat($('#dt-bills tr .long')[0].innerHTML);
			_valued = parseFloat($('#dt-bills tr .valued')[0].innerHTML);
			

			var data = [ {
				label : "本地",
				data : _local
			}, {
				label : "漫游",
				data : _roam
			}, {
				label : "长话",
				data : _long
			}, {
				label : "增值",
				data : _valued
			} ];

			var plotObj = $.plot($("#pie-recent"), data, {
				series : {
					pie : {
						show : true
					}
				},
				 label: {
		                show: true
		            },
				grid : {
					hoverable : true
				},
				tooltip : true,
				tooltipOpts : {
					content : "%p.0%, %s", // show percentages, rounding to 2 decimal places
					shifts : {
						x : 20,
						y : 0
					},
					defaultTheme : false
				}
			});
			
			_locals = $('#dt-bills tr .local');
			_roams = $('#dt-bills tr .roam');
			_longs = $('#dt-bills tr .long');
			_valueds = $('#dt-bills tr .valued');
			
			i=0;
			_local = 0;
			_roam = 0;
			_long = 0;
			_valued=0;
			
			for(i=0;i<_locals.length;i++)
				_local += parseFloat(_locals[i].innerHTML);
			_local = _local / i;
			
			for(i=0;i<_roams.length;i++)
				_roam += parseFloat(_roams[i].innerHTML);
			_roam = _roam / i;
			
			for(i=0;i<_longs.length;i++)
				_long += parseFloat(_longs[i].innerHTML);
			_long= _long/ i;
			
			for(i=0;i<_valueds.length;i++)
				_valued += parseFloat(_valueds[i].innerHTML);
			_valued = _valued / i;
			
			data = [ {
				label : "本地",
				data : _local
			}, {
				label : "漫游",
				data : _roam
			}, {
				label : "长话",
				data : _long
			}, {
				label : "增值",
				data : _valued
			} ];

			plotObj = $.plot($("#pie-avg"), data, {
				series : {
					pie : {
						show : true
					}
				},
				 label: {
		                show: true
		            },
				grid : {
					hoverable : true
				},
				tooltip : true,
				tooltipOpts : {
					content : "%p.0%, %s", // show percentages, rounding to 2 decimal places
					shifts : {
						x : 20,
						y : 0
					},
					defaultTheme : false
				}
			});
			
			
			month = $('#dt-bills .month');
			income = $('#dt-bills .income');
			gprs = $('#dt-bills .gprs');
			call = $('#dt-bills .callnum');

			var dataIncome = [];
			var dataGprs = [];
			var dataVoice = [];
			var dataCall = [];
			for (var i = month.length - 1; i >= 0; i--) {
				dataIncome.push([ parseInt(month[i].innerHTML),
						parseFloat(income[i].innerHTML) ]);
				dataGprs.push([ parseInt(month[i].innerHTML),
						parseFloat(gprs[i].innerHTML) ]);
				
				dataVoice.push([parseInt(month[i].innerHTML),
						parseFloat(_locals[i].innerHTML) + parseFloat(_roams[i].innerHTML) +parseFloat(_longs[i].innerHTML) ]);
				dataCall.push([parseInt(month[i].innerHTML),
						parseInt(call[i].innerHTML) ]);		
			}

			var datasetIncome = [ {
				label : "收入",
				data : dataIncome,
				points : {
					symbol : "circle"
				}
			} ];
			var datasetGprs = [ {
				label : "GPRS流量",
				data : dataGprs,
				points : {
					symbol : "circle"
				}
			} ];
			var datasetVoice = [ {
				label : "语音话费",
				data : dataVoice,
				points : {
					symbol : "circle"
				}
			} ];
			var datasetCall = [ {
				label : "通话次数",
				data : dataCall,
				points : {
					symbol : "circle"
				}
			} ];

			var options = {
				series : {
					lines : {
						show : true
					},
					points : {
						radius : 3,
						fill : true,
						show : true
					}
				},
				xaxis : {
					minTickSize : 1,
					axisLabel : "日期(YYYYMM)",
					axisLabelUseCanvas : true,
					axisLabelFontSizePixels : 12,
					axisLabelPadding : 30
				},
				yaxis : {
					axisLabel : "收入（元）",
					axisLabelUseCanvas : true,
					axisLabelFontSizePixels : 12,
					axisLabelPadding : 30,
				},
				tooltip : true,
				tooltipOpts : {
					content : "%x.0 收入为 %y.2",
					shifts : {
						x : -60,
						y : 25
					}
				},
				legend : {
					noColumns : 0,
					labelBoxBorderColor : "#000000",
					position : "nw"
				},
				grid : {
					hoverable : true,
					borderWidth : 2,
					borderColor : "#633200",
					backgroundColor : {
						colors : [ "#ffffff", "#EDF5FF" ]
					}
				},
				colors : [ "#FF0000" ]
			};

			$.plot($("#income-placeholder"), datasetIncome,options);

			options = {
				series : {
					lines : {
						show : true
					},
					points : {
						radius : 3,
						fill : true,
						show : true
					}
				},
				xaxis : {
					minTickSize : 1,
					axisLabel : "日期(YYYYMM)",
					axisLabelUseCanvas : true,
					axisLabelFontSizePixels : 12,
					axisLabelPadding : 30
				},
				yaxis : {
					axisLabel : "GPRS流量(M)",
					axisLabelUseCanvas : true,
					axisLabelFontSizePixels : 12,
					axisLabelPadding : 30,
				},
				tooltip : true,
				tooltipOpts : {
					content : "%x.0 流量为  %y.2",
					shifts : {
						x : -60,
						y : 25
					}
				},
				legend : {
					noColumns : 0,
					labelBoxBorderColor : "#000000",
					position : "nw"
				},
				grid : {
					hoverable : true,
					borderWidth : 2,
					borderColor : "#633200",
					backgroundColor : {
						colors : [ "#ffffff", "#EDF5FF" ]
					}
				},
				colors : [ "#0000FF" ]
			};
			$.plot($("#gprs-placeholder"), datasetGprs, options);
			
			options = {
					series : {
						lines : {
							show : true
						},
						points : {
							radius : 3,
							fill : true,
							show : true
						}
					},
					xaxis : {
						minTickSize : 1,
						axisLabel : "日期(YYYYMM)",
						axisLabelUseCanvas : true,
						axisLabelFontSizePixels : 12,
						axisLabelPadding : 30
					},
					yaxis : {
						axisLabel : "语音话费(元)",
						axisLabelUseCanvas : true,
						axisLabelFontSizePixels : 12,
						axisLabelPadding : 30,
					},
					tooltip : true,
					tooltipOpts : {
						content : "%x.0 话费 %y.2 元",
						shifts : {
							x : -60,
							y : 25
						}
					},
					legend : {
						noColumns : 0,
						labelBoxBorderColor : "#000000",
						position : "nw"
					},
					grid : {
						hoverable : true,
						borderWidth : 2,
						borderColor : "#633200",
						backgroundColor : {
							colors : [ "#ffffff", "#EDF5FF" ]
						}
					},
					colors : [ "#02a15f" ]
				};
				$.plot($("#voice-placeholder"), datasetVoice, options);
				
				options = {
						series : {
							lines : {
								show : true
							},
							points : {
								radius : 3,
								fill : true,
								show : true
							}
						},
						xaxis : {
							minTickSize : 1,
							axisLabel : "日期(YYYYMM)",
							axisLabelUseCanvas : true,
							axisLabelFontSizePixels : 12,
							axisLabelPadding : 30
						},
						yaxis : {
							axisLabel : "通话次数",
							axisLabelUseCanvas : true,
							axisLabelFontSizePixels : 12,
							axisLabelPadding : 30,
						},
						tooltip : true,
						tooltipOpts : {
							content : "%x.0 通话  %y.0次",
							shifts : {
								x : -60,
								y : 25
							}
						},
						legend : {
							noColumns : 0,
							labelBoxBorderColor : "#000000",
							position : "nw"
						},
						grid : {
							hoverable : true,
							borderWidth : 2,
							borderColor : "#633200",
							backgroundColor : {
								colors : [ "#ffffff", "#EDF5FF" ]
							}
						},
						colors : [ "#02a15f" ]
					};
					$.plot($("#callnumber-placeholder"), datasetCall, options);
					
					
					$('[data-toggle="popover-left"]').popover({placement:'left',trigger: 'hover',html:true});
		});
	</script>
</body>

</html>
