
/**
 * 封装一个公用的方法  
 * @param {Object} grid table的id  
 * @param {Object} func 获取异步数据的方法  
 * @param {Object} time 延时执行时间  
 */  
function Exec_Wait(grid,func,time,params){   
    var dalayTime = time;   
    __func_=func;   
    __selector_ = '#' + grid;   
    $(__selector_).datagrid("loading");
    gTimeout=window.setTimeout(function(){_Exec_Wait_(params);},dalayTime);   
}   
function _Exec_Wait_(params){   
    try{
    	if(__func_ == 'load_consumer_detail') {
    		load_consumer_detail(params['phone'],params['data']);
    	}
    }catch(e){   
        alert("__func_:" + __func_ + ";_ExecWait_" + e.message);   
    }finally{   
        window.clearTimeout(gTimeout);   
        $(__selector_).datagrid("loaded");   
    }   
}  

__phone__ = 'n_U_l_L';

function ajax_get_consumer_data(phone,theMonth,force){
	
	if($("#ck").is(':checked') == true) {
		ajax_get_consumer_recent_data(phone, theMonth, force);
	}
	else {
		if(__phone__ != phone || force) {
			$.ajax({
				  type: "GET",
				  url: "getConsumerBillsJson",
				  data: { "phoneNo": phone, "theMonth": theMonth},
				  
				  success:function(data){
					  Exec_Wait('tt','load_consumer_detail',50,{'phone':phone,'data':data});
				  	}
			});
			__phone__ = phone;
		}
	}
}

function ajax_get_consumer_recent_data(phone,theMonth,force){
	
	if(__phone__ != phone || force) {
		$.ajax({
			  type: "GET",
			  url: "getRecentConsumerBillsJson",
			  data: { "phoneNo": phone, "theMonth": theMonth},
			  
			  success:function(data){
				  Exec_Wait('tt','load_consumer_detail',50,{'phone':phone,'data':data});
			  	}
		});
		__phone__ = phone;
	}
}


$(function(){
	$('#datePicker').date_input();
	d = new Date();
	month = d.getMonth() + 1;
	if(month < 10)
		month = "0" + month;
	date = d.getDate();
	if(date < 10)
		date = "0" + date;
	dateString = d.getFullYear()+"-"+ month + "-" + date; 
	$('#datePicker').val(dateString);
	$('#currentMonth').html(d.getFullYear()+"年"+ month +"月");
	
	$('#datePicker').bind('change', function(e){
		yearMonth = $('#datePicker').val().split('-');
		$('#currentMonth').html(yearMonth[0] + "年" + yearMonth[1] + "月");
	});
	
	$(document).keyup(function (e) {
	    var key = e.which;
	    if (key == 27) {
	    	$('#w').window('close');
	    }
	});
	
	$(function(){ 
		if ($.browser.msie) { 
			$('#ck').click(function () { 
				this.blur(); 
				this.focus(); 
			}); 
		}; 
		$("#ck").change(function() { 
			if($("#ck").is(':checked') == true) {
				ajax_get_consumer_recent_data(__phone__, '201303', true);
			}
			else{
				ajax_get_consumer_data(__phone__, '201303', true);
			}
		}); 
	}); 
	
});

$(function(){
	$('#tt').datagrid({
		height:"100px",
		columns:[[
		          {field:'yearMonth',title:'月份'},
		          {field:'income',title:'收入'},
		          {field:'monthlyRental',title:'月租'},
		          {field:'localVoiceFee',title:'本地通话费'},
		          {field:'roamingFee',title:'漫游费'},
		          {field:'longDistanceVoiceFee',title:'长途通话费'},
		          {field:'valueAddedFee',title:'增值费'},
		          {field:'otherFee',title:'其他费'},
		          {field:'grantFee',title:'赠款金额'},
		          {field:'callNumber',title:'通话次数'},
		          {field:'localCallDuration',title:'本地通话时长'},
		          {field:'roamCallDuration',title:'漫游计费时长'},
		          {field:'longDistanceCallDuration',title:'长途计费时长'},
	//			  {field:'internalCallDuration',title:'国内长途计费时长'},
	//			  {field:'internationalCallDuration',title:'国际长途计费时长'},
		          {field:'sms',title:'点对点短信 '},
		          {field:'gprs',title:'GPRS流量(M)'}
		]],
		 rowStyler:function(index,row){
		        if (index == 0){    
		            return 'background-color:#FCF8E3;color:blue;';    
		        }    
		    }
	});
});

function load_consumer_detail(phone,data) {
//	$('#ccc').html("({\"total\" : " + data.length + ",\"rows\" : "+JSON.stringify(data)+"})");
	if(data.length == undefined) {
		$('#tt').datagrid('loadData',eval("({\"total\" : 1,\"rows\" : ["+JSON.stringify(data)+"]})")); 
		$('#tt').datagrid({height:"90px"});
	}
	else{
		$('#tt').datagrid('loadData',eval("({\"total\" : " + data.length + ",\"rows\" : "+JSON.stringify(data)+"})")); 
		$('#tt').datagrid({height:"210px"});
	}
	
}

