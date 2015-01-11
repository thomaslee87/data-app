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


jQuery(function() {
		
	if($('.dtInfo').length > 0) {
		$('.dtInfo')[0].innerHTML = 
		    '<b><span id="sDay">本日任务（<span id="timedate-day"></span>）</b>' +
		    '<b><span id="sWeek" style="display:none">本周任务（<span id="timedate-week-start"></span>~<span id="timedate-week-end"></span>）</b>' +
		    '<b><span id="sMon" style="display:none">本月任务（<span id="timedate-month-start"></span>~<span id="timedate-month-end"></span>）</b>';
	}
	
    //系统日期
    if($('#timedate').length > 0){
        var timedate = $.pony.util.datePattern("yyyy年MM月dd日，EEE");
        $('#timedate').html(timedate);
    }
    if($('#timedate-day').length > 0){
        var timedate = $.pony.util.datePattern("yyyy-MM-dd");
        $('#timedate-day').html(timedate);
    }
    if($('#timedate-week-start').length > 0){
        var timedate = $.pony.util.datePattern("yyyy-MM-dd");
        var week = $.pony.util.getWeek(timedate);
        $('#timedate-week-start').html($.pony.util.dateFormat(week.start,'yyyy-MM-dd'));
        if($('#timedate-week-end').length > 0){
            $('#timedate-week-end').html($.pony.util.dateFormat(week.end,'yyyy-MM-dd'));
        }
    }
    if($('#timedate-month-start').length > 0){
        var timedate = $.pony.util.datePattern("yyyy-MM-dd");
        var month = $.pony.util.getMonth(timedate);
        $('#timedate-month-start').html($.pony.util.dateFormat(month.start,'yyyy-MM-dd'));
        if($('#timedate-month-end').length > 0){
            $('#timedate-month-end').html($.pony.util.dateFormat(month.end,'yyyy-MM-dd'));
        }
    }

    //初始化日期选择器
	if($('.dtPicker').length > 0) {
		$('.dtPicker')[0].innerHTML = 
			'<span>请选择日期：</span><input type="text" class="form-control" style="width:200px;height:30px;float:right;margin-bottom:5px;cursor:pointer;background-color:#ffffff" readonly="true"/>';
		
		$('.dtPicker input').datepicker({
		    format: "yyyy-mm-dd",
		    todayBtn: "linked",
		    language: "zh-CN",
		    todayHighlight: true,
		    autoclose: true
		}).on('changeDate',function(ev){
			//系统日期
		    if($('#timedate').length > 0){
		        var timedate = $.pony.util.datePattern("yyyy年MM月dd日，EEE",ev.target.value);
		        $('#timedate').html(timedate);
		    }
		    if($('#timedate-day').length > 0){
		        var timedate = $.pony.util.datePattern("yyyy-MM-dd",ev.target.value);
		        $('#timedate-day').html(timedate);
		    }
		    if($('#timedate-week-start').length > 0){
		        var timedate = $.pony.util.datePattern("yyyy-MM-dd",ev.target.value);
		        var week = $.pony.util.getWeek(timedate);
		        $('#timedate-week-start').html($.pony.util.dateFormat(week.start,'yyyy-MM-dd'));
		        if($('#timedate-week-end').length > 0){
		            $('#timedate-week-end').html($.pony.util.dateFormat(week.end,'yyyy-MM-dd'));
		        }
		    }
		    if($('#timedate-month-start').length > 0){
		        var timedate = $.pony.util.datePattern("yyyy-MM-dd",ev.target.value);
		        var month = $.pony.util.getMonth(timedate);
		        $('#timedate-month-start').html($.pony.util.dateFormat(month.start,'yyyy-MM-dd'));
		        if($('#timedate-month-end').length > 0){
		            $('#timedate-month-end').html($.pony.util.dateFormat(month.end,'yyyy-MM-dd'));
		        }
		    }
		    
		});
		
		var timedate = $.pony.util.datePattern("yyyy-MM-dd");
		$('.dtPicker input').val(timedate);
	}
	
    //初始化日期选择器
	if($('.taskView').length > 0) {
		$('.taskView')[0].innerHTML = 
			'<label class="radio-inline" style="font-size:14px"><input type="radio" checked name="inlineRadioOptions" id="rDay" value="day"> 日视图</label>'+
			'<label class="radio-inline" style="font-size:14px"><input type="radio" name="inlineRadioOptions" id="rWeek" value="week"> 周视图</label>'+
			'<label class="radio-inline" style="font-size:14px"><input type="radio" name="inlineRadioOptions" id="rMonth" value="month"> 月视图</label>';
		
		//$("#sDay").css("display", "none");
		
		$(":radio").click(function(){
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
			
			$.post(showContract);
		});
	}
	
});
