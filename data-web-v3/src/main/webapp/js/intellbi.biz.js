(function($) { 
    $.pony = $.pony || {
        version: "v1.0.0"
    };
    $.extend($.pony, {
        util: {
            //根据iframe对象获取iframe的window对象
            getFrameWindow: function (frame) {
                return frame && typeof(frame) === 'object' && frame.tagName === 'IFRAME' && frame.contentWindow;
            },
            //根据iframe对象获取iframe的document对象
            getFrameDocument: function (frame){
                return frame && typeof(frame)=='object' && frame.tagName == 'IFRAME' && frame.contentDocument || frame.contentWindow && frame.contentWindow.document || frame.document;
            },
            //获取url参数名
            getUrlParam: function(paramName){
                var reg = new RegExp("(^|&)"+ paramName +"=([^&]*)(&|$)");
                var r = window.location.search.substr(1).match(reg);
                if (r!=null) return unescape(r[2]); return null;
            },
            //判断是否ie6
            isIE6: function() {
                return ($.browser.msie && $.browser.version=="6.0") ? true : false
            },
            //获取url的get参数, param 要获取的参数名称，如果不传，则返回整个参数对象
            getURLParams: function(param) {
                var params = {};
                var href = /^http/i.test(this) ? this : window.location.toString();
                var uri = href.split("?");
                if (!uri[1])
                    return null;

                uri = uri[1].split("#")[0];

                var paramSet = uri.split("&");
                var temp = [];
                for (index in paramSet) {
                    temp = paramSet[index].split("=");
                    params[temp[0]] = temp[1];
                }

                if (param) {
                    if (params[param])
                        return params[param];
                    else
                        return null;
                } else {
                    return params;
                }
            },
            //去掉首尾空字符
            trim: function(str) {
                return str.replace(/(^\s*)|(\s*$)/g,"");
            },
            //日期格式化
            dateFormat : function(date, format) {
                if(date == null)
                    date = new Date();
                if (!format) {
                    format = "yyyy-MM-dd hh:mm:ss";
                }
                var o = {
                    "M+": date.getMonth() + 1,
                    // month
                    "d+": date.getDate(),
                    // day
                    "h+": date.getHours(),
                    // hour
                    "m+": date.getMinutes(),
                    // minute
                    "s+": date.getSeconds(),
                    // second
                    "q+": Math.floor((date.getMonth() + 3) / 3),
                    // quarter
                    "S": date.getMilliseconds()
                            // millisecond
                };

                if (/(y+)/.test(format)) {
                    format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
                }

                for (var k in o) {
                    if (new RegExp("(" + k + ")").test(format)) {
                        format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
                    }
                }
                return format;
            },
            /* 
            * 将 Date 转化为指定格式的String
            * 
            * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符
            * 
            * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
            * 
            * datePattern("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
            * 
            * datePattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04
            * 
            * datePattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04
            * 
            * datePattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04
            * 
            * datePattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
            */
            datePattern : function(fmt, dateStr) {
            	var date = new Date();
            	if(dateStr != null)
            		date = new Date(dateStr.replace(/-/g,   "/"));  
                var o = {
                    "M+" : date.getMonth() + 1, /* 月份 */
                    "d+" : date.getDate(), /* 日 */
                    "h+" : date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, /* 小时 */
                    "H+" : date.getHours(), /* 小时 */
                    "m+" : date.getMinutes(), /* 分 */
                    "s+" : date.getSeconds(), /* 秒 */
                    "q+" : Math.floor((date.getMonth() + 3) / 3), /* 季度 */
                    /* 毫秒 */
                    "S" : date.getMilliseconds()
                };
                var week = {
                    "0" : "\u65e5",
                    "1" : "\u4e00",
                    "2" : "\u4e8c",
                    "3" : "\u4e09",
                    "4" : "\u56db",
                    "5" : "\u4e94",
                    "6" : "\u516d"
                };
                if (/(y+)/.test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
                }
                if (/(E+)/.test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f" : "\u5468") : "") + week[date.getDay() + ""]);
                }
                for ( var k in o) {
                    if (new RegExp("(" + k + ")").test(fmt)) {
                        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                    }
                }
                return fmt;
            },

            //获取dom对象的当前位置
            getPosition: function(ele){
                var top = ele.offset().top,
                left = ele.offset().left,
                bottom = top + ele.outerHeight(),
                right = left + ele.outerWidth(),
                lmid = left + ele.outerWidth()/2,
                vmid = top + ele.outerHeight()/2;
        
                // iPad position fix
                if (/iPad/i.test(navigator.userAgent)) {
                    top -= $(window).scrollTop();
                    bottom -= $(window).scrollTop();
                    vmid -= $(window).scrollTop();
                }
                var position = {
                    leftTop: function(){
                        return {x: left, y: top};
                    },
                    leftMid: function(){
                        return {x: left, y: vmid};
                    },
                    leftBottom: function(){
                        return {x: left, y: bottom};
                    },
                    topMid: function(){
                        return {x: lmid, y: top};
                    },
                    rightTop: function(){
                        return {x: right, y: top};
                    },
                    rightMid: function(){
                        return {x: right, y: vmid};
                    },
                    rightBottom: function(){
                        return {x: right, y: bottom};
                    },
                    MidBottom: function(){
                        return {x: lmid, y: bottom};
                    },
                    middle: function(){
                        return {x: lmid, y: vmid};
                    }
                };
                return position;
            },
            //获取根域名
            getDomain: function(url){
                var domain = "null";
                var regex = /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\.?/;
                var match = regex.exec(url);
                if (typeof match != "undefined" && null != match) {
                    domain = match[0];
                }
                return domain;
            },
            openWin: function(url){
                var top=190;
                var whichsns = url.substr(url.lastIndexOf("snsType=")+8,1);
                if(whichsns == 4 || whichsns == 5){
                    var left = document.body.clientWidth > 820 ? (document.body.clientWidth-820)/2 : 0;
                    window.open(url, 'connect_window', 'height=700, width=820, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }else if(whichsns == 8){
                    var left = (document.body.clientWidth-580)/2;
                    window.open(url, 'connect_window', 'height=620, width=580, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }else if(whichsns == 9){
                    var left = document.body.clientWidth > 900 ? (document.body.clientWidth-900)/2 : 0;
                    window.open(url, 'connect_window', 'height=550, width=900, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }else{                                      
                    var left = (document.body.clientWidth-580)/2;
                    window.open(url, 'connect_window', 'height=420, width=580, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }
        
            },
            
            getWeek: function(datestr){//yyyy-mm-dd
                var date = new Date(Date.parse(datestr.replace(/-/g,"/")));
                var monday = new Date(date.getTime());  
                var sunday = new Date(date.getTime());  
                day = monday.getDay();
                if (day == 0)
                	day = 7;
                monday.setDate(monday.getDate()+1-day);  
                sunday.setDate(sunday.getDate()+7-day);
                return {start:monday, end:sunday};
            },
       
            getMonth: function(datestr){
                var date = new Date(Date.parse(datestr.replace(/-/g,"/")));
                var nowMonth = date.getMonth();
                var nowYear = date.getFullYear(); 
                var first = new Date(nowYear, nowMonth, 1);
                var nextMonthFirstDay = new Date(nowYear, nowMonth+1, 1);
                var last = new Date(nextMonthFirstDay-86400000);
                return {start:first, end:last};
            }
        }
    });

})(jQuery);

/*
* 创建标签
*/
function addTab(title, url){
    if ($('#tabs').tabs('exists', title)){
        $('#tabs').tabs('select', title);//选中并刷新
        var currTab = $('#tabs').tabs('getSelected');
        var url = $(currTab.panel('options').content).attr('src');
        if(url != undefined && currTab.panel('options').title != 'Home') {
            $('#tabs').tabs('update',{
                tab:currTab,
                options:{
                    content:createFrame(url)
                }
            })
        }
    } else {
        $('#tabs').tabs('add',{
            title:title,
            content:createFrame(url),
            closable:true
        });
    }
    tabClose();
}

function createFrame(url) {
    var s = '<div id="iframe-wraper"><iframe scrolling="auto" frameborder="0"  src="'+url+'" width="100%" height="100%"></iframe></div>';
    return s;
}
function tabClose() {
    /*双击关闭TAB选项卡*/
    $(".tabs-inner").dblclick(function(){
        var subtitle = $(this).children(".tabs-closable").text();
        $('#tabs').tabs('close',subtitle);
    });
}


function widgetMod(){
    var widget = $('.widget-mod');
    if (widget[0]) {
        var w_minimize = widget.find('.mod-tool .mod-tool-collapse');
        var widgetClosed = widget.find('.mod-tool .mod-tool-close');
                  
        w_minimize.on('click', function() {
            _this = $(this);
            if(_this.hasClass('mod-tool-expand')) {
                _this.closest('.mod-hd').next('.mod-bd').slideDown('200');
                _this.removeClass("mod-tool-expand");
            } else {
                _this.addClass('mod-tool-expand').closest('.mod-hd').next('.mod-bd').slideUp('200');
            }
        });

        widgetClosed.on('click', function() {
            _this = $(this);
            _this.closest('.widget-mod').hide();
        });

       // $('.work-wraper').equalHeights();
        $('.work-wraper').each(function(){
            var currentTallest = 0;
            $(this).children().each(function(i){
                if ($(this).height() > currentTallest) { currentTallest = $(this).height(); }
            });
            if ($.browser.msie && $.browser.version == 6.0) { $(this).children().css({'height': currentTallest}); }
            $(this).children().css({'min-height': currentTallest}); 
        });

    }
}

function refreshDate(bizdate) {
	
	//系统日期
	if($('#timedate').length > 0){
	    var timedate = $.pony.util.datePattern("yyyy年MM月dd日，EEE",bizdate);
	    $('#timedate').html(timedate);
	}
	if($('#timedate-day').length > 0){
	    var timedate = $.pony.util.datePattern("yyyy-MM-dd",bizdate);
	    $('#timedate-day').html(timedate);
	}
	if($('#timedate-week-start').length > 0){
	    var timedate = $.pony.util.datePattern("yyyy-MM-dd",bizdate);
	    var week = $.pony.util.getWeek(timedate);
	    $('#timedate-week-start').html($.pony.util.dateFormat(week.start,'yyyy-MM-dd'));
	    if($('#timedate-week-end').length > 0){
	        $('#timedate-week-end').html($.pony.util.dateFormat(week.end,'yyyy-MM-dd'));
	    }
	}
	if($('#timedate-month-start').length > 0){
	    var timedate = $.pony.util.datePattern("yyyy-MM-dd",bizdate);
	    var month = $.pony.util.getMonth(timedate);
	    $('#timedate-month-start').html($.pony.util.dateFormat(month.start,'yyyy-MM-dd'));
	    if($('#timedate-month-end').length > 0){
	        $('#timedate-month-end').html($.pony.util.dateFormat(month.end,'yyyy-MM-dd'));
	    }
	}
	
}

if($.fn.dataTableExt) {
	
	//additional functions for data table
	$.fn.dataTableExt.oApi.fnPagingInfo = function (oSettings) {
	    return {
	        "iStart": oSettings._iDisplayStart,
	        "iEnd": oSettings.fnDisplayEnd(),
	        "iLength": oSettings._iDisplayLength,
	        "iTotal": oSettings.fnRecordsTotal(),
	        "iFilteredTotal": oSettings.fnRecordsDisplay(),
	        "iPage": Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
	        "iTotalPages": Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength)
	    };
	};
	
	$.extend($.fn.dataTableExt.oPagination, {
		"intellbi_numbers": {
			"fnInit": function (oSettings, nPaging, fnDraw) {
		        var oLang = oSettings.oLanguage.oPaginate;
		        var fnClickHandler = function (e) {
		            e.preventDefault();
		            if (oSettings.oApi._fnPageChange(oSettings, e.data.action)) {
		                fnDraw(oSettings);
		            }
		        };
		
		        $(nPaging).addClass('pagination').append(
		        		'<ul class="pagination">' +
		                '<li class="first disabled"><a href="#">' + oLang.sFirst + '</a></li>' +
		                '<li class="prev disabled"><a href="#">' + oLang.sPrevious + '</a></li>' +
		                '<li class="active"><a>第<span id="cur"></span>页/共<span id="ttl"></span>页</a></li>' +
		                '<li class="next disabled"><a href="#">' + oLang.sNext + '</a></li>' +
		                '<li class="last disabled"><a href="#">' + oLang.sLast + '</a></li>' +
		                '</ul>'
		        );
		        var els = $('a', nPaging);
		        $(els[0]).bind('click.DT', { action: "first" }, fnClickHandler);
		        $(els[1]).bind('click.DT', { action: "previous" }, fnClickHandler);
		        $(els[3]).bind('click.DT', { action: "next" }, fnClickHandler);
		        $(els[4]).bind('click.DT', { action: "last" }, fnClickHandler);
		    },
			
		    "fnUpdate": function ( oSettings, fnDraw )
			{
		        var oPaging = oSettings.oInstance.fnPagingInfo();
		        $('#cur').text(oPaging.iPage + 1);
		        $('#ttl').text(oPaging.iTotalPages);
		        
		        var an = oSettings.aanFeatures.p;
		        
		        for (i = 0, iLen = an.length; i < iLen; i++) {
		        	var els = $('li', an[i]);
		            // add / remove disabled classes from the static elements
		            if (oPaging.iPage === 0) {
		                $(els[0]).addClass('disabled');
		                $(els[1]).addClass('disabled');
		            } else {
		            	$(els[0]).removeClass('disabled');
		            	$(els[1]).removeClass('disabled');
		            }
		
		            if (oPaging.iPage === oPaging.iTotalPages - 1 || oPaging.iTotalPages === 0) {
		            	$(els[3]).addClass('disabled');
		                $(els[4]).addClass('disabled');
		            } else {
		            	$(els[3]).removeClass('disabled');
		            	$(els[4]).removeClass('disabled');
		            }
		        }
		    }
		}
	});

}


$(document).ready(function() {
	
	if($('.box-content').length > 0) {
		$('.box-content').tooltip({
		    selector: "[data-toggle=tooltip]",
		    container: "body"
		});
	}
	
	if($('.dtPicker input').length > 0) {
		$('.dtPicker input').datepicker({
		    format: "yyyy-mm-dd",
		    todayBtn: "linked",
		    language: "zh-CN",
		    todayHighlight: true,
		    autoclose: true
		}).on('changeDate',function(ev){
			refreshDate(ev.target.value);
		});
		
		var timedate = $.pony.util.datePattern("yyyy-MM-dd");
		$('.dtPicker input').val(timedate);
	}
	
	if($('#dt-contract').length > 0) {
	    contract_table = $('#dt-contract').dataTable({
	          "sPaginationType": "intellbi_numbers",
	          "bSort":false,
	          "bFilter" : false,
	          "bLengthChange" : false,
	          "bServerSide" : true,
	          "bDeferRender":true,
	          "iDisplayLength" : 10,
	          "sAjaxSource": 'data-web-v3/getContract', 
	          "bProcessing":true,
	          "sDom":'<"H"f<"dtInfo">>tr<"F"ip>',
	          
	          "aoColumns": [
	               {
	            	   "mRender":function(mData){
	            		   if(mData.isphoto == 1)
	            			   return "<i class=\"glyphicon glyphicon-camera\" title=\"拍照用户\"></i>  "+mData.phone;
	            		   else
	            			   return mData.phone;
	            	   }
	               },
	               { "sWidth": "30px" },
	               null,null,null,null,
	               {
	            	   
	            	   "mRender":function(mData){
	            		   sv4g = '预计平均每月可节省 ' + mData.save4g;
	            		   if(mData.save4g < 0)
	            			   sv4g = '预计平均每月将多消费 ' + -1 *mData.save4g;   
	            			   
	            		   return '<a href="billDetail?theBizMonth='+ mData.month+'&phoneNo='+mData.phone +'" target=_blank class="btn btn-primary btn-xs" data-toggle="popover-left" ' +
	            		   	'data-content=\'<p><i class="fa fa-hand-o-right">&nbsp;</i><span class="label label-success">续约套餐:</span> <ul><li><span class="label label-danger">' + mData.rmd + 
	            		   	'</span></li><li>预计平均每月可节省 ' + mData.save + '元 </li></ul></p><p> <i class="fa fa-hand-o-right">&nbsp;</i><span class="label label-success">带宽升级:</span> <ul><li>' + 
	            		   	'<span class="label label-danger">（4G）' + mData.rmd4g + '</span></li><li>' + sv4g + '元</li></ul></p>\''+
	            		   	'title=\'<center><span class="label label-warning">套餐推荐</span></center>\'' + // + mData.rmd + '\'>' +
	            		   	'<i class="glyphicon glyphicon-zoom-in icon-white"></i>详情</span></a>'
	            		   	;
//		            		   '<a href="#" class="btn btn-primary btn-xs" data-toggle="popover-left"' + 
//		            		   'data-content="预计平均每月可节省 "' + mData + '"/>元"' +
//		            		   'title=\'<span class="label label-warning">荐</span>/>\'>' +
//								'<i class="glyphicon glyphicon-zoom-in icon-white"></i>详情</a>';
	            		   
	            	   }
	               },
	           ],
	           
	           "fnServerParams": function (aoData) {
                   aoData.push({"name": "view", "value": $("input[name='inlineRadioOptions']:checked").val()});
	        	   aoData.push({"name":"bizdate", "value": $('.dtPicker input').val()});
	        	   aoData.push({"name":"orderby", "value":$('#orderby').val()});
	        	   if($('#hideDone').length > 0)
	        		   aoData.push({"name":"hideDone", "value":$('#hideDone').prop('checked')});
               },
	          
	          "fnServerData" : function(sSource, aoData, fnCallback) {
	              $.ajax({  
	                  "type" : 'post',
	                  "url" : sSource,
	                  "contentType" : "application/x-www-form-urlencoded",
	                  "dataType" : "json",
	                  "data" : {  
	                      aoData : JSON.stringify(aoData)
	                  },  
	                  "success" : function(resp) {  
	                      fnCallback(resp);  
//	                      alert(JSON.stringify(resp));
	                      if(resp['errMsg'] != null) {
	                    	  alert(errMsg);
	                      }
	                    	  
	        			  $('[data-toggle="popover-left"]').popover({placement:'left',trigger: 'hover',html:true});
	                  }  
	              });
	          },
	          
	          "oLanguage": {
	              	"sProcessing": "数据加载中...",
	              	"sLengthMenu": "每页显示 _MENU_",
	              	"sZeroRecords": "暂无数据",
	              	"sInfo": "第_START_ ~ _END_ 项，共 _TOTAL_ 项",
	              	"sInfoEmpty": "无可显示的数据",
	              	"sInfoFiltered": "(共   _MAX_ 项)",
	              	"sInfoPostFix": "",
	              	"sSearch": "过滤 ",
	              	"sUrl": "",
	              	"oPaginate": {
	              		"sFirst":    "首页",
	              		"sPrevious": "上一页",
	              		"sNext":     "下一页",
	              		"sLast":     "尾页"
	              	}
	          }
	      });
	    
	    $('div.dtInfo').addClass('alert alert-warning').html( 
			    '<b><span id="sDay" style="font-size:16px">本日任务（<span id="timedate-day"></span>）</b>' +
			    '<b><span id="sWeek" style="display:none;font-size:16px">本周任务（<span id="timedate-week-start"></span>~<span id="timedate-week-end"></span>）</b>' +
			    '<b><span id="sMon" style="display:none;;font-size:16px">本月任务（<span id="timedate-month-start"></span>~<span id="timedate-month-end"></span>）</b>'
		);
	}

	refreshDate();
	
	//初始化日期选择器
	if($('.dtPicker').length > 0) {
//		$('.dtPicker')[0].innerHTML = 
//			'<span>请选择日期：</span><input type="text" class="form-control" style="width:200px;height:30px;float:right;margin-bottom:5px;cursor:pointer;background-color:#ffffff" readonly="true"/>';
		
		$('.dtPicker input').datepicker({
		    format: "yyyy-mm-dd",
		    todayBtn: "linked",
		    language: "zh-CN",
		    todayHighlight: true,
		    autoclose: true
		}).on('changeDate',function(ev){
			refreshDate(ev.target.value);
			if($('#dt-contract').length > 0) {
				contract_table.fnDraw();//重新加载数据
			}
			if($('#dt-brandup').length > 0){
				brandup_table.fnDraw();
			}
		});
		
		var timedate = $.pony.util.datePattern("yyyy-MM-dd");
		$('.dtPicker input').val(timedate);
	}
	
	if($('#hideDone').length > 0) {
		$('#hideDone').bind('click', function(){
			if($('#dt-contract').length > 0) {
				contract_table.fnDraw();//重新加载数据
			}
			if($('#dt-brandup').length > 0){
				brandup_table.fnDraw();
			}
		});
	}
	
	if($('#taskView').length > 0) {
//		$('.taskView')[0].innerHTML = 
//			'<label class="radio-inline" style="font-size:14px"><input type="radio" checked name="inlineRadioOptions" id="rDay" value="day"> 日视图</label>'+
//			'<label class="radio-inline" style="font-size:14px"><input type="radio" name="inlineRadioOptions" id="rWeek" value="week"> 周视图</label>'+
//			'<label class="radio-inline" style="font-size:14px"><input type="radio" name="inlineRadioOptions" id="rMonth" value="month"> 月视图</label>';
		
		//$("#sDay").css("display", "none");
		
		$(":radio").change(function(){
			$('#sDay').css('display','none');
			$('#sWeek').css('display','none');
			$('#sMon').css('display','none');
			var val = $(this).val();
			if(val == "day") {
				$('#sDay').css('display','block');
			} else if(val == "week") {
				$('#sWeek').css('display','block');
			} else {
				$('#sMon').css('display','block');
			}
			if(contract_table != null) {
				contract_table.fnDraw();//重新加载数据
			}
		});
	}
	
	if($('#orderby').length > 0) {
		$('select').on('change', function (e) {
			
			if($('#dt-contract').length > 0) {
				contract_table.fnDraw();//重新加载数据
			}
			if($('#dt-brandup').length > 0){
				brandup_table.fnDraw();
			}
		});	
	}
	
	if($('#dt-brandup').length > 0) {
	    brandup_table = $('#dt-brandup').dataTable({
	          "sPaginationType": "intellbi_numbers",
	          "bSort":false,
	          "bFilter" : false,
	          "bLengthChange" : false,
	          "bServerSide" : true,
	          "bDeferRender":true,
	          "iDisplayLength" : 10,
	          "sAjaxSource": 'data-web-v3/getBrandUpConsumers', 
	          "bProcessing":true,
	          "sDom":'<"H"f<"dtInfo">>tr<"F"ip>',
	          
	          "aoColumns": [
	               {
	            	   "mRender":function(mData){
	            		   if(mData.isphoto4g == 1)
	            			   return "<i class=\"glyphicon glyphicon-camera\" title=\"4G拍照用户\"></i>  "+mData.phone;
	            		   else
	            			   return mData.phone;
	            	   }
	               },
	               { "sWidth": "30px" },
	               null,null,null,null,null,
	               {
	            	   "mRender":function(mData){
	            		   if(mData == 1)
	            			   return '是';
	            		   else
	            			   return '否';
	            	   }
	               },
	               {
	            	   
	            	   "mRender":function(mData){
	            		   sv = '预计平均每月可节省 ' + mData.save;
	            		   if(mData.save < 0)
	            			   sv = '预计平均每月将多消费 ' + -1 *mData.save; 
	            		   
	            		   return '<a href="brandUpDetail?theBizMonth='+ mData.month+'&phoneNo='+mData.phone +'" target=_blank class="btn btn-primary btn-xs" data-toggle="popover-left" ' +
	            		   	'data-content="' + sv + '元" ' + 
	            		   	'title=\'<span class="label label-warning">荐:</span>' + mData.rmd + '\'>' +
	            		   	'<i class="glyphicon glyphicon-zoom-in icon-white"></i>带宽升级</span></a>';
//		            		   '<a href="#" class="btn btn-primary btn-xs" data-toggle="popover-left"' + 
//		            		   'data-content="预计平均每月可节省 "' + mData + '"/>元"' +
//		            		   'title=\'<span class="label label-warning">荐</span>/>\'>' +
//								'<i class="glyphicon glyphicon-zoom-in icon-white"></i>详情</a>';
	            		   
	            	   }
	               },
	           ],
	           
	           "fnServerParams": function (aoData) {
	        	   aoData.push({"name":"bizdate", "value": $('.dtPicker input').val()});
	        	   aoData.push({"name":"orderby", "value":$('#orderby').val()});
	        	   if($('#hideDone').length > 0)
	        		   aoData.push({"name":"hideDone", "value":$('#hideDone').prop('checked')});
               },
	          
	          "fnServerData" : function(sSource, aoData, fnCallback) {
	              $.ajax({  
	                  "type" : 'post',
	                  "url" : sSource,
	                  "contentType" : "application/x-www-form-urlencoded",
	                  "dataType" : "json",
	                  "data" : {  
	                      aoData : JSON.stringify(aoData)
	                  },  
	                  "success" : function(resp) {  
	                      fnCallback(resp);  
//	                      alert(JSON.stringify(resp));
	                      if(resp['errMsg'] != null) {
	                    	  alert(errMsg);
	                      }
	                    	  
	                      $('div.dtInfo').addClass('alert alert-warning').html('最新数据来自：<b><span id="dataDesc" style="font-size:16px">' + resp['theDataMonth'] + '</span></b>');
	                      
	        			  $('[data-toggle="popover-left"]').popover({placement:'left',trigger: 'hover',html:true});
	                  }  
	              });
	          },
	          
	          "oLanguage": {
	              	"sProcessing": "数据加载中...",
	              	"sLengthMenu": "每页显示 _MENU_",
	              	"sZeroRecords": "暂无数据",
	              	"sInfo": "第_START_ ~ _END_ 项，共 _TOTAL_ 项",
	              	"sInfoEmpty": "无可显示的数据",
	              	"sInfoFiltered": "(共   _MAX_ 项)",
	              	"sInfoPostFix": "",
	              	"sSearch": "过滤 ",
	              	"sUrl": "",
	              	"oPaginate": {
	              		"sFirst":    "首页",
	              		"sPrevious": "上一页",
	              		"sNext":     "下一页",
	              		"sLast":     "尾页"
	              	}
	          }
	      });
	    
//	    $('div.dtInfo').addClass('alert alert-warning').html('数据来自：<b><span id="dataDesc" style="font-size:16px"></span></b>');
	}

});
	
