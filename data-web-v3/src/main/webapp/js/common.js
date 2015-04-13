$(function() {

    $('#side-menu').metisMenu();
    
});

//Loads the correct sidebar on window load,
//collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function() {
    $(window).bind("load resize", function() {
        topOffset = 50;
        width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
        if (width < 768) {
            $('div.navbar-collapse').addClass('collapse');
            topOffset = 100; // 2-row-menu
        } else {
            $('div.navbar-collapse').removeClass('collapse');
        }

        height = (this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $("#page-wrapper").css("min-height", (height) + "px");
        }
    });
    
});


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

