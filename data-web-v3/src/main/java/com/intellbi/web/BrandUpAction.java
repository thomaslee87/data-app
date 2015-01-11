package com.intellbi.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.intellbi.dao.TelecomPackageDao;
import com.intellbi.dataobject.BiDataDO;
import com.intellbi.dataobject.ConsumerBillDO;
import com.intellbi.dataobject.Pagination;
import com.intellbi.dataobject.TelecomPackageDO;
import com.intellbi.dataobject.UserDO;
import com.intellbi.service.BiDataService;
import com.intellbi.service.ConsumerBillService;
import com.intellbi.utils.Constants;
import com.intellbi.utils.MyDateUtils;
import com.intellbi.utils.MyDateUtils.Period;
import com.intellbi.utils.Tools;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

//响应datatables ajax请求
public class BrandUpAction extends ActionSupport{

	private static final long serialVersionUID = 3336731333982806609L;

	private Logger logger = Logger.getLogger(BrandUpAction.class);
	
	@Autowired
	private ConsumerBillService consumerBillService;
	
	@Autowired
	private TelecomPackageDao telPakcageDao;
	
	@Autowired
	private BiDataService biDataService;
	
	private int userId = -1;
	private String phoneNo;
	private String ordertype;
	
	private String theDataMonth;
	
	public String getTheDataMonth() {
		return theDataMonth;
	}

	public void setTheDataMonth(String theDataMonth) {
		this.theDataMonth = theDataMonth;
	}

	private String aoData;
	
	public String getAoData() {
		return aoData;
	}

	public void setAoData(String aoData) {
		this.aoData = aoData;
	}
	
	private Map<String,Object> dataMap;

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
	
	private boolean checkTheMonth(String theMonth){
		if(MyDateUtils.checkMonthFormat(theMonth, "yyyyMMdd")){
		} else if(MyDateUtils.checkMonthFormat(theMonth, "yyyyMM")){
		} else {
			return false;
		}
		return true;
	}
	
	/** 
	 * 获取当前登陆vip经理名下的所有合约用户，action传入参数包括：
	 * 工作周期(day,week,month)、选择日期(yyyy-mm-dd)、datatables控件带的参数(起始记录，每页记录数)
	 */
	public String getBrandUpConsumers(){
		String errMsg = "";
		JSONObject json = JSONObject.fromObject(aoData);
		if(json == null) {
			errMsg = "抱歉！暂时无法获取您需要的数据，请确认您已经登陆系统。";
			dataMap.put("errMsg", errMsg);
			return SUCCESS;
		}
		logger.info(aoData);

		dataMap = new HashMap<String, Object>();
		
		try {
			String sEcho = json.getString("sEcho");
			String iColumns = json.getString("iColumns");
			int iDisplayStart = Tools.parseInt(json.getString("iDisplayStart"));
			int iDisplayLength = Tools.parseInt(json.getString("iDisplayLength"));
			
			String bizdate = json.getString("bizdate");
			
			String orderby = json.getString("orderby");
			
			boolean _hideDone = json.getBoolean("hideDone");
			int hideDone = 0;
			if(_hideDone)
			    hideDone = 1;
			
			if(StringUtils.isBlank(bizdate)) {
				errMsg = "请先选择一个日期，用于获取合约即将到期的客户。";
				dataMap.put("errMsg", errMsg);
				return SUCCESS;
			}
			bizdate = MyDateUtils.transferDateFormat(bizdate, "yyyy-mm-dd", "yyyymmdd");
			if(StringUtils.isBlank(bizdate)) {
				errMsg = "系统捕捉到非法请求，请确认选择的日期为YYYY-MM-DD的格式。";
				dataMap.put("errMsg", errMsg);
				return SUCCESS;
			}
			
			Subject subject = SecurityUtils.getSubject();
	        if(subject.isAuthenticated() && iDisplayLength > 0 && iDisplayStart >= 0){
	            UserDO userDO = (UserDO)subject.getSession().getAttribute("userDO");
	            userId = userDO.getId();
	            if(userId == 1) //admin
	                userId = 0;
	            List<BiDataDO> biDataDOList = biDataService.findAll();
	            if(biDataDOList == null || biDataDOList.size() == 0) {
	            	errMsg = "系统尚未收集到可用数据。";
	    			dataMap.put("errMsg", errMsg);
	            	return SUCCESS;
	            }
	//          BiDataDO biDataDO = biDataService.getRecentBiDataDO();//最新的数据月份，从该月分表中加载数据
	            Period taskTimeWindow = getTaskTimeWindow(bizdate, Constants.TASK_VIEW_MONTH);
	            
	            String theBizMonth = MyDateUtils.getTheMonthOfBizDate(taskTimeWindow.getEnd(), "yyyymmdd");
	            String theMonth = biDataDOList.get(0).getTheMonth();
	            //使用小于等于选中月份的最新数据
	            for(BiDataDO biDataDO: biDataDOList) {
	            	if(biDataDO.getTheMonth().compareTo(theBizMonth) <= 0) {
	            		theMonth = biDataDO.getTheMonth();
	            		break;
	            	}
	            }
	            
	            theDataMonth = theMonth;
	            
	            Period cQueryWindow = getQueryWindow(taskTimeWindow,Constants.TASK_VIEW_MONTH);
	            
	            int cConsumerCnt = consumerBillService.getBrandUpCnt(userId, theMonth, cQueryWindow,-1, hideDone,theBizMonth);
	            
	//            int recordsTotal = consumerBillService.getTotalCount(theMonth, phoneNo,userId,-1);
	            
	            dataMap.put("draw", sEcho);
	            dataMap.put("recordsTotal", String.valueOf(cConsumerCnt));
	            dataMap.put("recordsFiltered", String.valueOf(cConsumerCnt));
	//            List<ConsumerBillDO>
	            dataMap.put("data", getDataList(userId,bizdate,theMonth,cQueryWindow,orderby,iDisplayStart,iDisplayLength,hideDone,theBizMonth));
	            dataMap.put("theDataMonth", theDataMonth.substring(0,4) + "年" + theDataMonth.substring(4) + "月");
	            return SUCCESS;
	        }
		} catch (Exception e) {
			errMsg = "系统捕捉到非法请求，请确认您已经登陆系统，或者重新登录后尝试。";
			dataMap.put("errMsg", errMsg);
			return SUCCESS;
		}
        return ERROR;
	}
	
	/**
	 * @return 返回查询时间窗（6月内）
	 * 由于合约期是按月生效，按天或者按周一般都没有数据（除非180天后的窗口正好包含了月末那天）
	 * 想保留按日和按周分配任务的思路，因此做一个额外处理，
	 * 天任务是从月任务中按续约和保有顺序各推荐5个用户，周各推荐20个
	 */
	private Period getQueryWindow(Period taskTimeWindow, String view) {
		//contract的query window，task的window平移180天
		Period cQueryWindow = taskTimeWindow.moveByDays(Constants.CONTRACT_DAYS);
		cQueryWindow.setBegin(taskTimeWindow.getBegin());
		return cQueryWindow;
	}
	
	/**
	 * @param theDate
	 * @param view
	 * 根据任务视图，获取任务时间窗
	 */
	private Period getTaskTimeWindow(String bizDate, String view) {
		if(StringUtils.isNotBlank(view)) { 
			if(view.equals(Constants.TASK_VIEW_DAY)){
				return new Period(bizDate, bizDate);
			} else if(view.equals(Constants.TASK_VIEW_WEEK)) {
				return MyDateUtils.getWeekOfDate(bizDate);
			} else if(view.equals(Constants.TASK_VIEW_MONTH)) {
				return MyDateUtils.getMonthOfDate(bizDate);
			}
		}
		return null;

/*		if(StringUtils.isBlank(view)) //参数不合法，返回的query会让得到的列表为空
			return new Period("0", "-1");
		if(view.equals(Constants.TASK_VIEW_DAY)) {
			int date = Tools.parseInt(bizDate);
			return new ContractQuery(date, date);
		} else if(view.equals(Constants.TASK_VIEW_WEEK)) {
			Period week = MyDateUtils.getWeekOfDate(bizDate);
			return new ContractQuery(Tools.parseInt(week.getBegin()),Tools.parseInt(week.getEnd()));
		} else if(view.equals(Constants.TASK_VIEW_MONTH)) {
			Period month = MyDateUtils.getMonthOfDate(bizDate);
			return new ContractQuery(Tools.parseInt(month.getBegin()),Tools.parseInt(month.getEnd()));
		} else {
			return new ContractQuery(0, -1);
		}
*/	}
	
	private List<List<Object>> getDataList(int userId, String bizdate,String theMonth, Period cQueryWindow, String orderby, int pageStart, int pageSize, int hideDone,String theBizMonth){
		List<List<Object>> dataList = new ArrayList<List<Object>>();
        try {
        	//读数据库加载一次
	        Map<String,String> idPkgMap = new HashMap<String, String>();
	        List<TelecomPackageDO> packages = telPakcageDao.getAll4G();
	        for(TelecomPackageDO pkg: packages) {
	            idPkgMap.put(String.valueOf(pkg.getId()), pkg.getName());
	        }
	        
            if(StringUtils.isBlank(orderby) || (!orderby.equals(Constants.ORDER_BY_GPRS6) && !orderby.equals(Constants.ORDER_BY_VALUD_CHANGE_4G)))
                orderby = Constants.ORDER_BY_GPRS6;
            
            Pagination pagination = new Pagination(pageStart, pageSize, orderby);
            List<ConsumerBillDO> bills = consumerBillService.getBrandUpConsumers(userId, theMonth, cQueryWindow, pagination, -1,hideDone,theBizMonth);
            
//            List<ConsumerBillDO> bills = null = consumerBillService.getAllMonthBills(theMonth.substring(0,6), phoneNo, userId, currPage, pageSize,ordertype,-1);
            
            for(ConsumerBillDO consumerBillDO: bills) {
                int contractTo = (int)(consumerBillDO.getContractTo());
//                consumerBillDO.setContractRemain(MyDateUtils.getMonthsBetween(Integer.parseInt(theMonth.substring(0,6)), contractTo));
                    
                if(consumerBillDO.getPriority() <= 3)
                  consumerBillDO.setPriorityDesc("高");
                else if(consumerBillDO.getPriority() >=8 )
                  consumerBillDO.setPriorityDesc("低");
                else
                  consumerBillDO.setPriorityDesc("中");
                
                if(consumerBillDO.getValueChange4G() > 10)
                  consumerBillDO.setValueChangeDesc("节省");
                else if(consumerBillDO.getValueChange4G() < -10)
                  consumerBillDO.setValueChangeDesc("增加");
                else
                  consumerBillDO.setValueChangeDesc("持平");
                
                consumerBillDO.setRecommend1("暂无更合适套餐推荐.");
                consumerBillDO.setRecommendCost1(0);
                String[] rcmdPackageIds = consumerBillDO.getRecommend4G().split(",");
                int rcmdNumber = rcmdPackageIds.length;
                if(rcmdNumber >= 1){
                    String[] rcmdPackagePair = rcmdPackageIds[0].split(":");
                    if(rcmdPackagePair.length == 2) {   
                        String rcmd1 = idPkgMap.get(rcmdPackagePair[0]);
                        if(StringUtils.isNotBlank(rcmd1)) {
                            double costSave = Math.round(consumerBillDO.getIncome6()) - Math.round(Double.parseDouble(rcmdPackagePair[1]));
//                            if(costSave > 0) {
                                consumerBillDO.setRecommend1(rcmd1);
                                consumerBillDO.setRecommendCost1(costSave);
//                            }
                        }
                    }
                }
                
                List<Object> _dtList = new ArrayList<Object>();
                Map<String,String> phoneMap = new HashMap<String,String>();
                phoneMap.put("phone", consumerBillDO.getPhoneNo());
                phoneMap.put("isphoto4g", String.valueOf(consumerBillDO.getIsPhoto4G()));
                _dtList.add(phoneMap);
                if(orderby.equals(Constants.ORDER_BY_GPRS6)) {
//                	_dtList.add(consumerBillDO.getGprs6());
                	double g = consumerBillDO.getGprs6();
                	if(g > 1000) 
                		_dtList.add("大于1000M");
                	else if(g > 500)
                		_dtList.add("500~1000M");
                	else if(g > 200)
                		_dtList.add("200~500M");
                	else if(g > 100)
                		_dtList.add("100~200M");
                	else
                		_dtList.add("小于100M");
                		
                }
                else
                	_dtList.add(consumerBillDO.getValueChangeDesc());	
                _dtList.add(consumerBillDO.getIncome());
                _dtList.add(consumerBillDO.getIncome6());
                _dtList.add(consumerBillDO.getGprs());
                _dtList.add(consumerBillDO.getGprs6());
//                _dtList.add(consumerBillDO.getContractRemain());
                _dtList.add(consumerBillDO.getPackageName());
                _dtList.add(consumerBillDO.getIsContractConsumer());
                
                Map<String,String> _map = new HashMap<String,String>();
                _map.put("month", bizdate.substring(0,6));
                _map.put("phone", consumerBillDO.getPhoneNo());
                _map.put("save", consumerBillDO.getRecommendCost1()+"");
                _map.put("rmd", "（4G）"+consumerBillDO.getRecommend1());
                _map.put("isphoto4g", String.valueOf(consumerBillDO.getIsPhoto4G()));
                _dtList.add(_map);
                dataList.add(_dtList);
            }
        } catch(Exception e) {
        	e.printStackTrace();
        }
        return dataList;
	}


}
