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

<title>错误</title>
    
    <!-- Bootstrap Core CSS -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="/css/plugins/dataTables.bootstrap.css"">

    <!-- MetisMenu CSS -->
    <link href="/css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="/css/plugins/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/css/common.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="/css/plugins/morris.css" rel="stylesheet">
    
    <link href='/css/plugins/datePicker.css'  rel='stylesheet'/>

    <!-- Custom Fonts -->
    <link href="/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="/js/html5shiv.js"></script>
        <script src="/js/respond.js"></script>
    <![endif]-->

</head>

<p/><p/><p/>
    <div class="col-lg-10 ">
        <div class="panel panel-red">
            <div class="panel-heading">
                错误
            </div>
            <div class="panel-body">
                <p>
                	<c:out value="${info}"/>
                </p>
            </div>
            <div class="panel-footer">
                
            </div>
        </div>
        <!-- /.col-lg-10 -->
    </div>

    <script src="/js/jquery-1.11.0.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="/js/plugins/metisMenu/metisMenu.min.js"></script>

    <!-- DataTables JavaScript -->
    <script src="/js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="/js/plugins/dataTables/dataTables.bootstrap.js"></script>
    
    <script src="/js/plugins/bootstrap-datepicker.js"></script>
    
    <script src="/js/plugins/popover.js"></script>

</html>
